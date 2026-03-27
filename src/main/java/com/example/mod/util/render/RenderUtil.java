package com.example.mod.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.awt.Font;

public class RenderUtil {
    private static CustomFontRenderer customFont = null;
    private static boolean useProductFont = false;
    
    public static void drawRect(float x, float y, float width, float height, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y + height, 0.0D).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlineRect(float x, float y, float width, float height, float lineWidth, int color) {
        drawRect(x, y, width, lineWidth, color);
        drawRect(x, y + height - lineWidth, width, lineWidth, color);
        drawRect(x, y, lineWidth, height, color);
        drawRect(x + width - lineWidth, y, lineWidth, height, color);
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        net.minecraft.client.gui.ScaledResolution scale = new net.minecraft.client.gui.ScaledResolution(Minecraft.getMinecraft());
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static void scissor(float x, float y, float width, float height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        prepareScissorBox(x, y, x + width, y + height);
    }

    public static void releaseScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static int applyOpacity(int color, float opacity) {
        Color c = new Color(color, true);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (c.getAlpha() * opacity)).getRGB();
    }

    public static void setUseProductFont(boolean enabled) {
        useProductFont = enabled;
        if (enabled && customFont == null) {
            initCustomFont();
        }
    }

    public static boolean isUseProductFont() {
        return useProductFont;
    }

    private static void initCustomFont() {
        try {
            customFont = new CustomFontRenderer("/assets/lchax/fonts/productsans.ttf", 16);
        } catch (Exception ignored) {
            try {
                customFont = new CustomFontRenderer(new Font("SansSerif", Font.PLAIN, 16), 16);
            } catch (Exception ignored2) {
                customFont = null;
                useProductFont = false;
            }
        }
    }

    public static void drawString(String text, float x, float y, int color) {
        if (useProductFont && customFont != null) {
            customFont.drawString(text, x, y, color);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        }
    }
    
    public static int getStringWidth(String text) {
        if (useProductFont && customFont != null) {
            return customFont.getStringWidth(text);
        }
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }
}
