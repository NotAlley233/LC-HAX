package com.example.mod.event;

public class ChatReceiveEvent {
    private final String message;

    public ChatReceiveEvent(String message) {
        this.message = message == null ? "" : message;
    }

    public String getMessage() {
        return message;
    }
}
