package com.example.service;

public enum BotCommand {
    START("/start"),
    LINK("/link"),
    FINISH("/finish");

    private final String name;

    BotCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
