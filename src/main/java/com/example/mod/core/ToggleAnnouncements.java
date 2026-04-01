package com.example.mod.core;

import java.util.concurrent.atomic.AtomicInteger;

public final class ToggleAnnouncements {
    private static final AtomicInteger silenceDepth = new AtomicInteger(0);

    private ToggleAnnouncements() {}

    public static boolean enabled() {
        return silenceDepth.get() <= 0;
    }

    public static void pushSilent() {
        silenceDepth.incrementAndGet();
    }

    public static void popSilent() {
        int v = silenceDepth.decrementAndGet();
        if (v < 0) silenceDepth.set(0);
    }
}

