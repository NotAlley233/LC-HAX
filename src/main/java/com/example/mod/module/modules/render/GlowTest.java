package com.example.mod.module.modules.render;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.OverlayRenderable;
import com.example.mod.util.render.GlowRenderer;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;
import net.minecraft.client.Minecraft;

/**
 * Renders a rounded panel with outer glow (blur) to validate {@link GlowRenderer}.
 */
public class GlowTest extends BaseModule implements OverlayRenderable {

    private int panelX = 40;
    private int panelY = 40;
    private int panelWidth = 168;
    private int panelHeight = 72;
    private float cornerRadius = 10.0f;
    private float glowBlurRadius = 14.0f;
    private float glowExposure = 2.5f;
    private int glowRed = 80;
    private int glowGreen = 180;
    private int glowBlue = 255;
    private int panelBgAlpha = 180;

    public GlowTest() {
        super("glowtest", "Test panel for outer glow rendering (GlowRenderer).", Category.RENDER, false);
    }

    public int getPanelX() {
        return panelX;
    }

    public void setPanelX(int panelX) {
        this.panelX = Math.max(0, Math.min(2000, panelX));
    }

    public int getPanelY() {
        return panelY;
    }

    public void setPanelY(int panelY) {
        this.panelY = Math.max(0, Math.min(2000, panelY));
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public void setPanelWidth(int panelWidth) {
        this.panelWidth = Math.max(32, Math.min(600, panelWidth));
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public void setPanelHeight(int panelHeight) {
        this.panelHeight = Math.max(24, Math.min(600, panelHeight));
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = Math.max(0.0f, Math.min(64.0f, cornerRadius));
    }

    public float getGlowBlurRadius() {
        return glowBlurRadius;
    }

    public void setGlowBlurRadius(float glowBlurRadius) {
        this.glowBlurRadius = Math.max(1.0f, Math.min(128.0f, glowBlurRadius));
    }

    public float getGlowExposure() {
        return glowExposure;
    }

    public void setGlowExposure(float glowExposure) {
        this.glowExposure = Math.max(0.1f, Math.min(16.0f, glowExposure));
    }

    public int getGlowRed() {
        return glowRed;
    }

    public void setGlowRed(int glowRed) {
        this.glowRed = clamp255(glowRed);
    }

    public int getGlowGreen() {
        return glowGreen;
    }

    public void setGlowGreen(int glowGreen) {
        this.glowGreen = clamp255(glowGreen);
    }

    public int getGlowBlue() {
        return glowBlue;
    }

    public void setGlowBlue(int glowBlue) {
        this.glowBlue = clamp255(glowBlue);
    }

    public int getPanelBgAlpha() {
        return panelBgAlpha;
    }

    public void setPanelBgAlpha(int panelBgAlpha) {
        this.panelBgAlpha = Math.max(0, Math.min(255, panelBgAlpha));
    }

    private static int clamp255(int v) {
        return Math.max(0, Math.min(255, v));
    }

    @Override
    public void onRenderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) {
            return;
        }

        float r = glowRed / 255.0f;
        float g = glowGreen / 255.0f;
        float b = glowBlue / 255.0f;

        final float px = panelX;
        final float py = panelY;
        final float pw = panelWidth;
        final float ph = panelHeight;
        final float cr = cornerRadius;

        GlowRenderer.drawOuterGlow(
                () -> RoundedUtils.drawRoundedRect(px, py, pw, ph, cr, 0xFFFFFFFF),
                glowBlurRadius,
                glowExposure,
                r,
                g,
                b
        );

        int bg = (panelBgAlpha << 24) | 0x1A1A1A;
        RoundedUtils.drawRoundedRect(px, py, pw, ph, cr, bg);

        float labelY = py + ph * 0.5f - 4.0f;
        RenderUtil.drawString("Glow test", px + 10.0f, labelY, 0xFFFFFF);
    }
}
