package com.example.mod.dynamicisland.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

/**
 * DynamicIsland font wrapper (independent from existing RenderUtil font path).
 */
public final class DIFontManager {
    private DIFontManager() {}

    public static final ScaledMcFont axiformaBold20 = new ScaledMcFont(1.0f, true);
    // Avoid fractional GL scaling to prevent blurriness.
    // Also enable shadow so the 2nd line matches the reference visual.
    public static final ScaledMcFont axiforma18 = new ScaledMcFont(1.0f, true);

    public static final class ScaledMcFont {
        private final float scale;
        private final boolean shadow;

        private ScaledMcFont(float scale, boolean shadow) {
            this.scale = scale;
            this.shadow = shadow;
        }

        public void drawString(String text, float x, float y, int color) {
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            if (fr == null || text == null) {
                return;
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, 1.0f);
            // Round coordinates to reduce texture sampling blur.
            float sx = Math.round(x / scale);
            float sy = Math.round(y / scale);
            fr.drawString(text, sx, sy, color, shadow);
            GlStateManager.popMatrix();
        }

        public int getStringWidth(String text) {
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            if (fr == null || text == null) {
                return 0;
            }
            return Math.round(fr.getStringWidth(text) * scale);
        }
    }
}
