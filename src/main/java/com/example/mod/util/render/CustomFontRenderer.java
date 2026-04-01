package com.example.mod.util.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CustomFontRenderer {
    private static final int TEX_SIZE = 512;
    private final DynamicTexture texture;
    private final int[] widths = new int[256];
    private final int[] charPosX = new int[256];
    private final int[] charPosY = new int[256];
    private final int cellHeight;

    public CustomFontRenderer(String fontPath, int size) {
        try (InputStream is = this.getClass().getResourceAsStream(fontPath)) {
            if (is == null) {
                throw new RuntimeException("Font not found: " + fontPath);
            }
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, (float) size);
            this.cellHeight = size + size / 2;
            this.texture = generateTexture(baseFont);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Failed to load font: " + fontPath, e);
        }
    }

    public CustomFontRenderer(Font font, int size) {
        Font baseFont = font.deriveFont(Font.PLAIN, (float) size);
        this.cellHeight = size + size / 2;
        this.texture = generateTexture(baseFont);
    }

    private DynamicTexture generateTexture(Font font) {
        BufferedImage img = new BufferedImage(TEX_SIZE, TEX_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        int x = 0;
        int y = 0;
        for (int i = 0; i < 256; i++) {
            char c = (char) i;
            if (c == '\n' || c == '\r') continue;
            int w = g.getFontMetrics().charWidth(c);
            if (w <= 0) w = 1;
            this.widths[i] = w;
            if (x + w > TEX_SIZE) {
                x = 0;
                y += this.cellHeight;
            }
            this.charPosX[i] = x;
            this.charPosY[i] = y;
            g.drawString(String.valueOf(c), x, y + this.cellHeight - 4);
            x += w;
        }
        g.dispose();
        return new DynamicTexture(img);
    }

    public void drawString(String text, float x, float y, int color) {
        if (text == null || text.isEmpty()) return;
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.bindTexture(this.texture.getGlTextureId());

        float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
        float red = (float) (color >> 16 & 0xFF) / 255.0f;
        float green = (float) (color >> 8 & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);

        float posX = x;
        GL11.glBegin(GL11.GL_QUADS);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 256) continue;
            int w = this.widths[c];
            if (w <= 0) continue;

            float u0 = (float) this.charPosX[c] / TEX_SIZE;
            float v0 = (float) this.charPosY[c] / TEX_SIZE;
            float u1 = (float) (this.charPosX[c] + w) / TEX_SIZE;
            float v1 = (float) (this.charPosY[c] + this.cellHeight) / TEX_SIZE;

            GL11.glTexCoord2f(u0, v0);
            GL11.glVertex2f(posX, y);
            GL11.glTexCoord2f(u0, v1);
            GL11.glVertex2f(posX, y + this.cellHeight);
            GL11.glTexCoord2f(u1, v1);
            GL11.glVertex2f(posX + w, y + this.cellHeight);
            GL11.glTexCoord2f(u1, v0);
            GL11.glVertex2f(posX + w, y);

            posX += w;
        }
        GL11.glEnd();

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopAttrib();
    }

    public int getStringWidth(String text) {
        if (text == null) return 0;
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 256) continue;
            width += this.widths[c];
        }
        return width;
    }

    public int getFontHeight() {
        return this.cellHeight;
    }
}
