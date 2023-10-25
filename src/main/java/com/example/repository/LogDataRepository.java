package com.example.repository;

import com.example.model.LogData;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogDataRepository implements PanacheMongoRepository<LogData> {
}