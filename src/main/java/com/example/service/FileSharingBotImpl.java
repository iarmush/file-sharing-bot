package com.example.service;

import com.example.config.BotConfig;
import jakarta.enterprise.inject.Produces;
import org.jboss.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class FileSharingBotImpl {

    private static final Logger LOGGER = Logger.getLogger(FileSharingBotImpl.class);
    private final BotConfig botConfig;
    private final MessageService messageService;

    public FileSharingBotImpl(BotConfig botConfig, MessageService messageService) {
        this.botConfig = botConfig;
        this.messageService = messageService;
    }

    @Produces
    public TelegramLongPollingBot telegramLongPollingBot() {
        return new TelegramLongPollingBot(botConfig.token()) {
            @Override
            public String getBotUsername() {
                return botConfig.username();
            }

            @Override
            public void onUpdateReceived(Update update) {
                try {
                    var message = update.getMessage();
                    if (message.hasText()) {
                        messageService.handleText(message);
                    } else if (message.hasDocument() || message.hasPhoto() || message.hasVideo()) {
                        messageService.handleMedia(message);
                    } else {
                        execute(SendMessage.builder().text("Please send media").build());
                    }
                } catch (TelegramApiException | IOException e) {
                    LOGGER.errorf(e, "Error while receive update");
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
