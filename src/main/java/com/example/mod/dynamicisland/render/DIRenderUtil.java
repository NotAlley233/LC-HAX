package com.example.mod.dynamicisland.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public final class DIRenderUtil {
    private DIRenderUtil() {}

    public static void drawRectWH(float x, float y, float width, float height, int color) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        wr.begin(7, DefaultVertexFormats.POSITION);
        wr.pos(x, y + height, 0.0).endVertex();
        wr.pos(x + width, y + height, 0.0).endVertex();
        wr.pos(x + width, y, 0.0).endVertex();
        wr.pos(x, y, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static int reAlpha(int color, int alpha) {
        int a = Math.max(0, Math.min(255, alpha));
        return (a << 24) | (color & 0x00FFFFFF);
    }
}
