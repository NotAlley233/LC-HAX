package com.example.mod.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayDeque;
import java.util.Deque;

public class RenderUtil {
    private static CustomFontRenderer customFont = null;
    private static boolean useProductFont = false;
    private static final Deque<int[]> scissorStack = new ArrayDeque<>();

    public static void drawRect(float x, float y, float width, float height, int color) {
        if (width <= 0 || height <= 0) return;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        wr.begin(7, DefaultVertexFormats.POSITION);
        wr.pos(x, y + height, 0.0).endVertex();
        wr.pos(x + width, y + height, 0.0).endVertex();
        wr.pos(x + width, y, 0.0).endVertex();
        wr.pos(x, y, 0.0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float x, float y, float width, float height,
                                         int startColor, int endColor, boolean horizontal) {
        if (width <= 0 || height <= 0) return;
        float sa = (float) (startColor >> 24 & 255) / 255.0F;
        float sr = (float) (startColor >> 16 & 255) / 255.0F;
        float sg = (float) (startColor >> 8 & 255) / 255.0F;
        float sb = (float) (startColor & 255) / 255.0F;
        float ea = (float) (endColor >> 24 & 255) / 255.0F;
        float er = (float) (endColor >> 16 & 255) / 255.0F;
        float eg = (float) (endColor >> 8 & 255) / 255.0F;
        float eb = (float) (endColor & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);

        if (horizontal) {
            wr.pos(x, y + height, 0).color(sr, sg, sb, sa).endVertex();
            wr.pos(x + width, y + height, 0).color(er, eg, eb, ea).endVertex();
            wr.pos(x + width, y, 0).color(er, eg, eb, ea).endVertex();
            wr.pos(x, y, 0).color(sr, sg, sb, sa).endVertex();
        } else {
            wr.pos(x, y + height, 0).color(er, eg, eb, ea).endVertex();
            wr.pos(x + width, y + height, 0).color(er, eg, eb, ea).endVertex();
            wr.pos(x + width, y, 0).color(sr, sg, sb, sa).endVertex();
            wr.pos(x, y, 0).color(sr, sg, sb, sa).endVertex();
        }

        tess.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void drawOutlineRect(float x, float y, float width, float height, float lineWidth, int color) {
        drawRect(x, y, width, lineWidth, color);
        drawRect(x, y + height - lineWidth, width, lineWidth, color);
        drawRect(x, y, lineWidth, height, color);
        drawRect(x + width - lineWidth, y, lineWidth, height, color);
    }

    public static void scissor(float x, float y, float width, float height) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int factor = sr.getScaleFactor();
        int sx = (int) (x * factor);
        int sy = (int) ((sr.getScaledHeight() - y - height) * factor);
        int sw = (int) (width * factor);
        int sh = (int) (height * factor);

        if (!scissorStack.isEmpty()) {
            int[] parent = scissorStack.peek();
            int px2 = parent[0] + parent[2];
            int py2 = parent[1] + parent[3];
            int cx2 = sx + sw;
            int cy2 = sy + sh;
            sx = Math.max(sx, parent[0]);
            sy = Math.max(sy, parent[1]);
            sw = Math.max(0, Math.min(cx2, px2) - sx);
            sh = Math.max(0, Math.min(cy2, py2) - sy);
        }

        scissorStack.push(new int[]{sx, sy, sw, sh});
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(sx, sy, sw, sh);
    }

    public static void releaseScissor() {
        if (!scissorStack.isEmpty()) {
            scissorStack.pop();
        }
        if (scissorStack.isEmpty()) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            int[] parent = scissorStack.peek();
            GL11.glScissor(parent[0], parent[1], parent[2], parent[3]);
        }
    }

    public static int applyOpacity(int color, float opacity) {
        Color c = new Color(color, true);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(),
                Math.max(0, Math.min(255, (int) (c.getAlpha() * opacity)))).getRGB();
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

    public static void drawFilledBoundingBox(AxisAlignedBB bb, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();

        wr.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();

        wr.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();

        wr.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        tessellator.draw();
    }
}
