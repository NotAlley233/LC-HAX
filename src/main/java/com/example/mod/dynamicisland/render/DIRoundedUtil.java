package com.example.mod.dynamicisland.render;

import org.lwjgl.opengl.GL11;

/**
 * DynamicIsland rounded rendering helper.
 * Reference style inspired by:
 * https://github.com/Jonqwq/DynamicIsland-System-for-mc-hacked-client
 * (MIT license, attribution retained)
 */
public final class DIRoundedUtil {
    private DIRoundedUtil() {}

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        float r = Math.max(0.0f, Math.min(radius, Math.min(width, height) * 0.5f));
        if (r <= 0.5f) {
            DIRenderUtil.drawRectWH(x, y, width, height, color);
            return;
        }

        float a = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor4f(red, green, blue, a);

        GL11.glBegin(GL11.GL_POLYGON);
        arc(x + r, y + r, r, 180, 270);
        arc(x + width - r, y + r, r, 270, 360);
        arc(x + width - r, y + height - r, r, 0, 90);
        arc(x + r, y + height - r, r, 90, 180);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

    private static void arc(float cx, float cy, float r, int start, int end) {
        for (int i = start; i <= end; i++) {
            double rad = Math.toRadians(i);
            GL11.glVertex2f(cx + (float) Math.cos(rad) * r, cy + (float) Math.sin(rad) * r);
        }
    }
}
