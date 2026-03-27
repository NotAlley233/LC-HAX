package com.example.mod.util.render;

public final class ShadowUtil {
    private ShadowUtil() {}

    public static void drawShadow(float x, float y, float width, float height, float radius, float size, int color) {
        float layers = Math.max(1f, size);
        for (int i = 0; i < layers; i++) {
            float t = (i + 1f) / layers;
            float alphaFactor = (1f - t) * 0.55f;
            int shadow = RenderUtil.applyOpacity(color, alphaFactor);
            float grow = t * size;
            RoundedUtils.drawRoundedRect(x - grow, y - grow, width + grow * 2f, height + grow * 2f, radius + grow * 0.35f, shadow);
        }
    }
}
