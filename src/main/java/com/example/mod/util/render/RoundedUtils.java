package com.example.mod.util.render;

public class RoundedUtils {

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        int alpha = (color >>> 24) & 0xFF;
        if (alpha == 0 && color != 0) {
            color = color | 0xFF000000;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        if (radius <= 0.5f) {
            RenderUtil.drawRect(x, y, width, height, color);
            return;
        }

        float r = Math.min(radius, Math.min(width, height) * 0.5f);
        RenderUtil.drawRect(x + r, y, width - 2 * r, height, color);
        RenderUtil.drawRect(x, y + r, r, height - 2 * r, color);
        RenderUtil.drawRect(x + width - r, y + r, r, height - 2 * r, color);
    }

    public static void drawOutlineRect(float v, float v1, float v2, float v3, float v4, int i, int rgb) {

    }
}
