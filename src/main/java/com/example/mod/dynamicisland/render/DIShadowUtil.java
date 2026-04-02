package com.example.mod.dynamicisland.render;

/**
 * DynamicIsland shadow helper.
 * Mirrors the layer/gaussian approach of {@code ShadowUtil}, but uses {@link DIRoundedUtils}
 * so we don't depend on the existing rounded-shadow toolchain.
 */
public final class DIShadowUtil {
    private DIShadowUtil() {}

    public static void drawShadow(
            float x,
            float y,
            float width,
            float height,
            float radius,
            float size,
            int color
    ) {
        int baseAlpha = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        int layers = Math.max(6, Math.min(20, (int) (size * 0.55f + 4f)));
        for (int i = layers; i >= 1; i--) {
            float t = (float) i / layers;
            float gaussian = (float) Math.exp(-3.4f * t * t);
            int a = Math.max(0, Math.min(255, (int) (baseAlpha * gaussian * 0.5f)));
            if (a <= 0) continue;

            int shadow = (a << 24) | (r << 16) | (g << 8) | b;
            float grow = t * size;
            DIRoundedUtils.drawRoundedRect(
                    x - grow,
                    y - grow,
                    width + grow * 2.0f,
                    height + grow * 2.0f,
                    radius + grow * 0.5f,
                    shadow
            );
        }
    }
}

