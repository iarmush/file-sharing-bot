package com.example.service;

import com.example.model.LogData;
import com.example.repository.LogDataRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

@ApplicationScoped
public class LogDataService {

    private static final Logger LOGGER = Logger.getLogger(LogDataService.class);

    private final LogDataRepository logDataRepository;

    public LogDataService(LogDataRepository logDataRepository) {
        this.logDataRepository = logDataRepository;
    }

    public void save(Long chatId) {
        LOGGER.infof("Attempt to save in %s", chatId);
        var logData = new LogData();
        logData.setChatId(chatId);
        logData.setDownloadDate(LocalDateTime.now());
        logDataRepository.persist(logData);
    }
}
