package com.example.mod.util.render;

import org.lwjgl.opengl.GL11;

public class RoundedUtils {

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        drawRoundedRect(x, y, width, height, radius, color, true, true, true, true);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color,
                                        boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        if (width <= 0 || height <= 0) return;
        int alphaCheck = (color >>> 24) & 0xFF;
        if (alphaCheck == 0 && color != 0) color = color | 0xFF000000;

        float r = Math.min(radius, Math.min(width, height) * 0.5f);
        if (r <= 0.5f || (!topLeft && !topRight && !bottomLeft && !bottomRight)) {
            RenderUtil.drawRect(x, y, width, height, color);
            return;
        }

        float a = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, a);

        GL11.glBegin(GL11.GL_POLYGON);

        if (topLeft) {
            addArcVertices(x + r, y + r, r, 180, 270);
        } else {
            GL11.glVertex2f(x, y);
        }

        if (topRight) {
            addArcVertices(x + width - r, y + r, r, 270, 360);
        } else {
            GL11.glVertex2f(x + width, y);
        }

        if (bottomRight) {
            addArcVertices(x + width - r, y + height - r, r, 0, 90);
        } else {
            GL11.glVertex2f(x + width, y + height);
        }

        if (bottomLeft) {
            addArcVertices(x + r, y + height - r, r, 90, 180);
        } else {
            GL11.glVertex2f(x, y + height);
        }

        // polygon is implicitly closed

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

    public static void drawCircle(float cx, float cy, float radius, int color) {
        if (radius <= 0) return;
        float a = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, a);

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(cx, cy);
        for (int i = 0; i <= 360; i += 2) {
            double rad = Math.toRadians(i);
            GL11.glVertex2f(cx + (float) Math.cos(rad) * radius, cy + (float) Math.sin(rad) * radius);
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

    private static void addArcVertices(float cx, float cy, float r, int startAngle, int endAngle) {
        for (int i = startAngle; i <= endAngle; i += 3) {
            double rad = Math.toRadians(i);
            GL11.glVertex2f(cx + (float) Math.cos(rad) * r, cy + (float) Math.sin(rad) * r);
        }
        double endRad = Math.toRadians(endAngle);
        GL11.glVertex2f(cx + (float) Math.cos(endRad) * r, cy + (float) Math.sin(endRad) * r);
    }

    public static void drawOutlineRect(float x, float y, float width, float height, float lineWidth, int color, int ignored) {
        RenderUtil.drawOutlineRect(x, y, width, height, lineWidth, color);
    }
}
