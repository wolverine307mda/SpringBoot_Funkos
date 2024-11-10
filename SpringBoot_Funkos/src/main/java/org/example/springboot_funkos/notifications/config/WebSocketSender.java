package org.example.springboot_funkos.notifications.config;

import java.io.IOException;

public interface WebSocketSender {
    void sendMessage(String message) throws IOException;
}