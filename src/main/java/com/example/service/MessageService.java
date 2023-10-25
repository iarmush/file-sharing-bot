package com.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;

@ApplicationScoped
public class MessageService {

    private static final Logger LOGGER = Logger.getLogger(MessageService.class);
    private final TelegramLongPollingBot telegramLongPollingBot;
    private final MinioService minioService;

    public MessageService(TelegramLongPollingBot telegramLongPollingBot, MinioService minioService) {
        this.telegramLongPollingBot = telegramLongPollingBot;
        this.minioService = minioService;
    }

    public void handleText(Message message) throws TelegramApiException {
        var chatId = message.getChatId();
        LOGGER.infof("Handled text in chatId: %s", chatId);

        if (message.getText().equals(BotCommand.START.getName())) {
            telegramLongPollingBot.execute(SendMessage.builder().text("""
                            Hello! It's File sharing bot.
                            Send me files, photos or videos.
                            After use command /link for getting external link for downloading.
                            """)
                    .chatId(chatId)
                    .build());
        } else if (message.getText().equals(BotCommand.LINK.getName())) {
            telegramLongPollingBot.execute(SendMessage.builder().text("""
                            Use this link for downloading archive:
                            http://localhost:8091/?chatId=%s
                            """.formatted(chatId))
                    .chatId(chatId)
                    .build());
        } else if (message.getText().equals(BotCommand.FINISH.getName())) {
            minioService.deleteFilesAndBucket(String.valueOf(chatId));
            telegramLongPollingBot.execute(SendMessage.builder()
                    .text("File sharing is ready for new medias")
                    .chatId(chatId)
                    .build());
        } else {
            telegramLongPollingBot.execute(SendMessage.builder()
                    .text("Unknown command")
                    .chatId(chatId)
                    .build());
        }
    }

    public void handleMedia(Message message) throws TelegramApiException, IOException {
        var chatId = message.getChatId();
        LOGGER.infof("Handled media in chatId: %s", chatId);

        String fileName;
        String fileId;
        String filePath;
        if (message.getDocument() != null) {
            fileName = message.getDocument().getFileName();
            fileId = message.getDocument().getFileId();
            filePath = telegramLongPollingBot.execute(new GetFile(fileId)).getFilePath();
        } else if (message.getPhoto() != null) {
            fileName = "photo_" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".jpeg";
            fileId = Collections.max(message.getPhoto(), Comparator.comparing(PhotoSize::getFileSize)).getFileId();
            filePath = telegramLongPollingBot.execute(new GetFile(fileId)).getFilePath();
        } else if (message.getVideo() != null) {
            fileName = message.getVideo().getFileName();
            fileId = message.getVideo().getFileId();
            filePath = telegramLongPollingBot.execute(new GetFile(fileId)).getFilePath();
        } else {
            LOGGER.errorf("Error while handling media in chatId: :s", chatId);
            throw new RuntimeException("Error while handling media in chatId: " + chatId);
        }

        var bytes = telegramLongPollingBot.downloadFileAsStream(filePath).readAllBytes();
        minioService.uploadFile(String.valueOf(chatId), fileName, bytes);
    }
}
