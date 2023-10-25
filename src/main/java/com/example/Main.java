package com.example;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {
        private static final Logger LOGGER = Logger.getLogger(MyApp.class);
        private final TelegramLongPollingBot telegramLongPollingBot;

        public MyApp(TelegramLongPollingBot telegramLongPollingBot) {
            this.telegramLongPollingBot = telegramLongPollingBot;
        }

        @Override
        public int run(String... args) {
            try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(telegramLongPollingBot);
            } catch (TelegramApiException e) {
                LOGGER.errorf(e, e.getMessage());
            }
            Quarkus.waitForExit();
            return 0;
        }
    }
}
