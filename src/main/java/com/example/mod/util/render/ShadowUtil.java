package com.example.mod.util.render;

public final class ShadowUtil {
    private ShadowUtil() {}

    public static void drawShadow(float x, float y, float width, float height, float radius, float size, int color) {
        int baseAlpha = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        int layers = Math.max(8, (int) size);
        for (int i = layers; i >= 1; i--) {
            float t = (float) i / layers;
            float gaussian = (float) Math.exp(-4.0 * t * t);
            int a = Math.max(0, Math.min(255, (int) (baseAlpha * gaussian * 0.45f)));
            if (a <= 0) continue;
            int shadow = (a << 24) | (r << 16) | (g << 8) | b;
            float grow = t * size;
            RoundedUtils.drawRoundedRect(
                    x - grow, y - grow,
                    width + grow * 2f, height + grow * 2f,
                    radius + grow * 0.5f, shadow);
        }
    }
}
