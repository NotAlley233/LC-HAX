package com.example.mod.util.render;

import org.lwjgl.opengl.GL11;

public class RoundedUtils {

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        drawRoundedRect(x, y, width, height, radius, color, true, true, true, true);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color, boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        int alpha = (color >>> 24) & 0xFF;
        if (alpha == 0 && color != 0) {
            color = color | 0xFF000000;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        if (radius <= 0.5f || (!topLeft && !topRight && !bottomLeft && !bottomRight)) {
            RenderUtil.drawRect(x, y, width, height, color);
            return;
        }

        float r = Math.min(radius, Math.min(width, height) * 0.5f);
        float leftInset = (topLeft || bottomLeft) ? r : 0f;
        float rightInset = (topRight || bottomRight) ? r : 0f;

        RenderUtil.drawRect(x + leftInset, y, width - leftInset - rightInset, height, color);
        RenderUtil.drawRect(x, y + (topLeft ? r : 0f), leftInset, height - (topLeft ? r : 0f) - (bottomLeft ? r : 0f), color);
        RenderUtil.drawRect(x + width - rightInset, y + (topRight ? r : 0f), rightInset, height - (topRight ? r : 0f) - (bottomRight ? r : 0f), color);

        if (!topLeft) {
            RenderUtil.drawRect(x, y, r, r, color);
        }
        if (!topRight) {
            RenderUtil.drawRect(x + width - r, y, r, r, color);
        }
        if (!bottomLeft) {
            RenderUtil.drawRect(x, y + height - r, r, r, color);
        }
        if (!bottomRight) {
            RenderUtil.drawRect(x + width - r, y + height - r, r, r, color);
        }

        drawCirclePart(x + r, y + r, r, 180, 270, color, topLeft);
        drawCirclePart(x + width - r, y + r, r, 270, 360, color, topRight);
        drawCirclePart(x + r, y + height - r, r, 90, 180, color, bottomLeft);
        drawCirclePart(x + width - r, y + height - r, r, 0, 90, color, bottomRight);
    }

    private static void drawCirclePart(float cx, float cy, float r, int start, int end, int color, boolean enabled) {
        if (!enabled || r <= 0f) return;
        float a = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, a);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(cx, cy);
        for (int i = start; i <= end; i += 4) {
            double rad = Math.toRadians(i);
            GL11.glVertex2f((float) (cx + Math.cos(rad) * r), (float) (cy + Math.sin(rad) * r));
        }
        GL11.glVertex2f((float) (cx + Math.cos(Math.toRadians(end)) * r), (float) (cy + Math.sin(Math.toRadians(end)) * r));
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

    public static void drawOutlineRect(float x, float y, float width, float height, float lineWidth, int color, int ignored) {
        RenderUtil.drawOutlineRect(x, y, width, height, lineWidth, color);
    }
}
