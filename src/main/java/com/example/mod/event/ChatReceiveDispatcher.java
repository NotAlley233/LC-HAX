package com.example.mod.event;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class ChatReceiveDispatcher {
    private static final CopyOnWriteArrayList<Consumer<ChatReceiveEvent>> LISTENERS = new CopyOnWriteArrayList<>();

    private ChatReceiveDispatcher() {
    }

    public static void subscribe(Consumer<ChatReceiveEvent> listener) {
        if (listener != null) {
            LISTENERS.addIfAbsent(listener);
        }
    }

    public static void unsubscribe(Consumer<ChatReceiveEvent> listener) {
        if (listener != null) {
            LISTENERS.remove(listener);
        }
    }

    public static void publish(String message) {
        ChatReceiveEvent event = new ChatReceiveEvent(message);
        for (Consumer<ChatReceiveEvent> listener : LISTENERS) {
            listener.accept(event);
        }
    }
}
