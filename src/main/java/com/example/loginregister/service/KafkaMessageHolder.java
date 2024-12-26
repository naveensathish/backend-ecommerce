package com.example.loginregister.service;

import org.springframework.stereotype.Component;

@Component
public class KafkaMessageHolder {
    private String lastConsumedMessage;

    public String getLastConsumedMessage() {
        return lastConsumedMessage;
    }

    public void setLastConsumedMessage(String lastConsumedMessage) {
        this.lastConsumedMessage = lastConsumedMessage;
    }
}
