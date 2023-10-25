package com.example.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@MongoEntity(collection = "logData")
public class LogData extends PanacheMongoEntity {

    @NotNull
    @BsonProperty("chatId")
    private Long chatId;

    @NotNull
    @BsonProperty("downloadDate")
    public LocalDateTime downloadDate;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(LocalDateTime downloadDate) {
        this.downloadDate = downloadDate;
    }
}
