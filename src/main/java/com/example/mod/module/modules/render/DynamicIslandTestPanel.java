package com.example.mod.module.modules.render;

import com.example.mod.dynamicisland.render.DIAnimationUtil;
import com.example.mod.dynamicisland.render.DIFontManager;
import com.example.mod.dynamicisland.render.DIShadowUtil;
import com.example.mod.dynamicisland.render.DIRoundedUtil;
import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.OverlayRenderable;
import com.example.mod.gui.clickgui.theme.MaterialTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Visual test panel for DynamicIsland style.
 * Reference style inspired by:
 * https://github.com/Jonqwq/DynamicIsland-System-for-mc-hacked-client
 * (MIT license, attribution retained)
 */
public class DynamicIslandTestPanel extends BaseModule implements OverlayRenderable {
    private static final String TITLE = "Module Toggled";
    private static final String ENABLED_TEXT = "KillAura has been \u00A72Enabled";
    private static final String DISABLED_TEXT = "KillAura has been \u00A74Disabled";

    private float renderX = 0.0f;
    private float width = 180.0f;
    private float height = 40.0f;
    private float switchAnim = 1.0f;

    private boolean enabledState = true;
    private boolean autoPulse = true;
    private int offsetY = 15;
    private int bgAlpha = 255;

    private long lastPulseAt = System.currentTimeMillis();

    public DynamicIslandTestPanel() {
        super("dynamicisland", "DynamicIsland style visual preview panel.", Category.RENDER, false);
    }

    public boolean isAutoPulse() {
        return autoPulse;
    }

    public void setAutoPulse(boolean autoPulse) {
        this.autoPulse = autoPulse;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = Math.max(0, Math.min(250, offsetY));
    }

    @Override
    public void onRenderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) {
            return;
        }

        long now = System.currentTimeMillis();
        if (autoPulse && now - lastPulseAt >= 1400L) {
            enabledState = !enabledState;
            lastPulseAt = now;
        }

        String desc = enabledState ? ENABLED_TEXT : DISABLED_TEXT;
        float targetWidth = 50.0f + Math.max(
                DIFontManager.axiformaBold20.getStringWidth(TITLE),
                DIFontManager.axiforma18.getStringWidth(desc)
        );
        float targetHeight = 40.0f;

        ScaledResolution sr = new ScaledResolution(mc);
        float targetX = (sr.getScaledWidth() - targetWidth) / 2.0f;

        renderX = DIAnimationUtil.smooth(renderX, targetX, 0.2f);
        width = DIAnimationUtil.smooth(width, targetWidth, 0.2f);
        height = DIAnimationUtil.smooth(height, targetHeight, 0.2f);
        switchAnim = DIAnimationUtil.smooth(switchAnim, enabledState ? 1.0f : 0.0f, 0.2f);

        float x = renderX;
        float y = offsetY;

        float radius = MaterialTheme.CORNER_RADIUS_PANEL;
        int shadowColor = 0x6E000000; // matches clickgui's ShadowUtil(0,0,0,(int)(110*progress)) when progress=1
        // clickgui 的 shadow blur size 用于大面板，这里按高度缩放，避免小胶囊显得“过大/过糊”
        float shadowSize = Math.max(6.0f, Math.min(22.0f, height * 0.35f));
        DIShadowUtil.drawShadow(x, y, width, height, radius, shadowSize, shadowColor);
        com.example.mod.dynamicisland.render.DIRoundedUtils.drawRoundedRect(
                x,
                y,
                width,
                height,
                radius,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.PANEL_BG, bgAlpha)
        );

        int trackColor = argb(
                255,
                (int) (128 - 80 * switchAnim),
                (int) (128 - 11 * switchAnim),
                (int) (128 + 19 * switchAnim)
        );

        DIRoundedUtil.drawRoundedRect(x + 6.0f, y + 13.0f, 26.0f, 14.0f, 6.5f, 0xFF404040);
        DIRoundedUtil.drawRoundedRect(x + 7.0f, y + 14.0f, 24.0f, 12.0f, 6.0f, trackColor);
        DIRoundedUtil.drawRoundedRect(x + 8.0f + 12.0f * switchAnim, y + 15.0f, 10.0f, 10.0f, 4.5f, 0xFF2B2B2B);

        DIFontManager.axiformaBold20.drawString(TITLE, x + 38.0f, y + 9.0f, 0xFFFFFFFF);
        DIFontManager.axiforma18.drawString(desc, x + 38.0f, y + 24.0f, 0xFFFFFFFF);
    }

    private static int argb(int a, int r, int g, int b) {
        int aa = Math.max(0, Math.min(255, a));
        int rr = Math.max(0, Math.min(255, r));
        int gg = Math.max(0, Math.min(255, g));
        int bb = Math.max(0, Math.min(255, b));
        return (aa << 24) | (rr << 16) | (gg << 8) | bb;
    }
}
