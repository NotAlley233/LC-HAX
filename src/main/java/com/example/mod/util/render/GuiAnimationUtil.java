package com.example.mod.util.render;

public final class GuiAnimationUtil {
    private GuiAnimationUtil() {}

    public static float approach(float current, float target, float speed) {
        return current + (target - current) * speed;
    }

    public static float easeOutCubic(float t) {
        float v = 1f - t;
        return 1f - v * v * v;
    }

    public static float clamp01(float value) {
        return Math.max(0f, Math.min(1f, value));
    }
}
