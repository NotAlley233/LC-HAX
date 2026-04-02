package com.example.mod.dynamicisland.render;

import com.example.mod.util.render.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * Rounded-rect renderer for DynamicIsland.
 * Ported from the clickgui rounded-rect approach to make shadows match visually.
 */
public final class DIRoundedUtils {
    private static ShaderUtil roundedRectShader;
    private static boolean roundedRectShaderFailed;

    private DIRoundedUtils() {}

    private static ShaderUtil getRoundedRectShader() {
        if (roundedRectShaderFailed) return null;
        if (roundedRectShader == null) {
            try {
                roundedRectShader = new ShaderUtil("roundedRect");
            } catch (Exception e) {
                roundedRectShaderFailed = true;
                return null;
            }
        }
        return roundedRectShader;
    }

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
            DIRenderUtil.drawRectWH(x, y, width, height, color);
            return;
        }

        boolean allCorners = topLeft && topRight && bottomLeft && bottomRight;
        if (allCorners && getRoundedRectShader() != null) {
            drawRoundedRectShader(x, y, width, height, r, color);
            return;
        }

        drawRoundedRectPolygon(x, y, width, height, r, color, topLeft, topRight, bottomLeft, bottomRight);
    }

    private static void drawRoundedRectShader(float x, float y, float width, float height, float radius, int color) {
        ShaderUtil shader = getRoundedRectShader();
        if (shader == null) {
            drawRoundedRectPolygon(x, y, width, height, radius, color, true, true, true, true);
            return;
        }

        float a = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        Minecraft mc = Minecraft.getMinecraft();
        int sf = new net.minecraft.client.gui.ScaledResolution(mc).getScaleFactor();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.alphaFunc(516, 0.0F);

        shader.init();
        shader.setUniformf("location", x * sf, (mc.displayHeight - (height * sf)) - (y * sf));
        shader.setUniformf("rectSize", width * sf, height * sf);
        shader.setUniformf("radius", radius * sf);
        shader.setUniformi("blur", 0);
        shader.setUniformf("color", red, green, blue, a);
        ShaderUtil.drawQuads(x - 1, y - 1, width + 2, height + 2);
        shader.unload();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void drawRoundedRectPolygon(float x, float y, float width, float height, float r, int color,
                                               boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
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

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

    private static void addArcVertices(float cx, float cy, float r, int startAngle, int endAngle) {
        for (int i = startAngle; i <= endAngle; i += 1) {
            double rad = Math.toRadians(i);
            GL11.glVertex2f(cx + (float) Math.cos(rad) * r, cy + (float) Math.sin(rad) * r);
        }
    }
}

