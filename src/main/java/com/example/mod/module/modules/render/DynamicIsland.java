package com.example.mod.module.modules.render;

import com.example.mod.dynamicisland.animation.DIDirection;
import com.example.mod.dynamicisland.animation.DIEaseOutExpo;
import com.example.mod.dynamicisland.render.DIAnimationUtil;
import com.example.mod.dynamicisland.render.DIShadowUtil;
import com.example.mod.dynamicisland.render.DIFontManager;
import com.example.mod.dynamicisland.render.DIRoundedUtils;
import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.OverlayRenderable;
import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

/**
 * DynamicIsland module based on reference visual style:
 * https://github.com/Jonqwq/DynamicIsland-System-for-mc-hacked-client
 * (MIT license attribution retained)
 */
public class DynamicIsland extends BaseModule implements OverlayRenderable {
    private static volatile DynamicIsland instance;

    private static final String TITLE = "Module Toggled";
    private static final long EXIT_FADE_MS = 220L;
    private static final long IDLE_TEXT_SWITCH_INTERVAL_MS = 3000L;
    private static final String IDLE_TEXT_MODE_CUSTOM_ONLY = "custom_only";
    private static final String IDLE_TEXT_MODE_CUSTOM_SWITCH = "custom_switch";

    private final List<ToggleEntry> entries = new ArrayList<>();

    private float renderX = 0.0f;
    private float width = 180.0f;
    private float height = 24.0f;

    private int offsetY = 15;
    private int durationMs = 2200;
    private int maxVisible = 2;
    private int bgAlpha = 130;
    private boolean shadowEnabled = true;

    // Idle text config: only used when there are no toggle entries visible.
    private String customText = "";
    private String idleTextMode = IDLE_TEXT_MODE_CUSTOM_ONLY;
    private long lastIdleTextSwitchAtMs = 0L;
    private boolean wasIdleLastFrame = false;

    public DynamicIsland() {
        super("dynamicisland", "DynamicIsland module toggle notifications.", Category.RENDER, false);
        instance = this;
    }

    public static void onModuleToggle(String moduleName, boolean enabled) {
        DynamicIsland di = instance;
        if (di == null || !di.enabled()) {
            return;
        }
        di.pushModuleToggle(moduleName, enabled);
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = Math.max(0, Math.min(250, offsetY));
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = Math.max(800, Math.min(8000, durationMs));
    }

    public int getMaxVisible() {
        return maxVisible;
    }

    public void setMaxVisible(int maxVisible) {
        this.maxVisible = Math.max(1, Math.min(4, maxVisible));
    }

    public int getBgAlpha() {
        return bgAlpha;
    }

    public void setBgAlpha(int bgAlpha) {
        this.bgAlpha = Math.max(0, Math.min(255, bgAlpha));
    }

    public boolean isShadowEnabled() {
        return shadowEnabled;
    }

    public void setShadowEnabled(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;
    }

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText == null ? "" : customText;
    }

    public String getIdleTextMode() {
        return idleTextMode;
    }

    public void setIdleTextMode(String idleTextMode) {
        String v = idleTextMode == null ? "" : idleTextMode;
        if (v.equalsIgnoreCase(IDLE_TEXT_MODE_CUSTOM_SWITCH)) {
            this.idleTextMode = IDLE_TEXT_MODE_CUSTOM_SWITCH;
        } else {
            this.idleTextMode = IDLE_TEXT_MODE_CUSTOM_ONLY;
        }
    }

    @Override
    protected void onDisable() {
        synchronized (entries) {
            entries.clear();
        }
    }

    private void pushModuleToggle(String moduleName, boolean enabled) {
        String name = moduleName == null ? "Module" : moduleName;
        long now = System.currentTimeMillis();

        synchronized (entries) {
            for (ToggleEntry e : entries) {
                if (e.moduleName.equalsIgnoreCase(name)) {
                    e.resetTime(now, enabled);
                    return;
                }
            }
            entries.add(new ToggleEntry(name, enabled, now));
            while (entries.size() > 12) {
                entries.remove(0);
            }
        }
    }

    @Override
    public void onRenderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) {
            return;
        }

        long now = System.currentTimeMillis();
        List<ToggleEntry> visible = collectVisibleEntries(now);
        boolean idle = visible.isEmpty();

        ScaledResolution sr = new ScaledResolution(mc);

        // Idle text switching state is updated only when entering/existing idle mode.
        if (idle) {
            if (!wasIdleLastFrame) {
                lastIdleTextSwitchAtMs = now;
            }
        } else {
            // Leaving idle mode: next time we enter idle, reset switch timer.
            lastIdleTextSwitchAtMs = 0L;
        }
        wasIdleLastFrame = idle;

        String idleText = null;
        if (idle) {
            idleText = getIdleText(mc, now);
        }

        float targetWidth = idle ? (16.0f + DIFontManager.axiforma18.getStringWidth(idleText))
                : getTargetWidthForVisible(visible);
        float targetHeight = idle ? 24.0f : 40.0f * visible.size();
        float targetX = (sr.getScaledWidth() - targetWidth) * 0.5f;

        renderX = DIAnimationUtil.smooth(renderX, targetX, 0.2f);
        width = DIAnimationUtil.smooth(width, targetWidth, 0.2f);
        height = DIAnimationUtil.smooth(height, targetHeight, 0.2f);

        float x = renderX;
        float y = offsetY;
        float radius = MaterialTheme.CORNER_RADIUS_PANEL;
        if (shadowEnabled) {
            float shadowSize = Math.max(6.0f, Math.min(22.0f, height * 0.35f));
            DIShadowUtil.drawShadow(x, y, width, height, radius, shadowSize, 0x6E000000);
        }
        DIRoundedUtils.drawRoundedRect(
                x, y, width, height, radius,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.PANEL_BG, bgAlpha)
        );

        RenderUtil.scissor(x, y, width, height);

        if (idle) {
            drawIdleState(idleText, x, y);
        } else {
            float renderY = y;
            for (ToggleEntry entry : visible) {
                drawEntry(entry, renderY, now);
                renderY += 40.0f;
            }
        }

        RenderUtil.releaseScissor();
    }

    private List<ToggleEntry> collectVisibleEntries(long now) {
        List<ToggleEntry> live = new ArrayList<>();
        synchronized (entries) {
            entries.removeIf(e -> now - e.createdAt > (long) durationMs + EXIT_FADE_MS);
            if (entries.isEmpty()) {
                return live;
            }
            int start = Math.max(0, entries.size() - maxVisible);
            for (int i = start; i < entries.size(); i++) {
                live.add(entries.get(i));
            }
        }
        return live;
    }

    private float getTargetWidthForVisible(List<ToggleEntry> visible) {
        if (!visible.isEmpty()) {
            float max = 140.0f;
            for (ToggleEntry entry : visible) {
                String desc = entry.enabled
                        ? entry.moduleName + " has been \u00A72Enabled"
                        : entry.moduleName + " has been \u00A74Disabled";
                float w = 50.0f + Math.max(
                        DIFontManager.axiformaBold20.getStringWidth(TITLE),
                        DIFontManager.axiforma18.getStringWidth(desc)
                );
                if (w > max) {
                    max = w;
                }
            }
            return max;
        }
        return 0.0f;
    }

    private String getBaseIdleText(Minecraft mc) {
        return "lchax | " + getSessionName(mc) + " | " + Minecraft.getDebugFPS() + "fps";
    }

    private String getIdleText(Minecraft mc, long now) {
        String base = getBaseIdleText(mc);
        if (customText == null || customText.isEmpty()) {
            return base;
        }

        if (idleTextMode.equalsIgnoreCase(IDLE_TEXT_MODE_CUSTOM_ONLY)) {
            return customText;
        }

        // idleTextMode == custom_switch
        long elapsed = Math.max(0L, now - lastIdleTextSwitchAtMs);
        long phase = elapsed / IDLE_TEXT_SWITCH_INTERVAL_MS;
        boolean showCustom = (phase % 2L) == 1L;
        return showCustom ? customText : base;
    }

    private void drawIdleState(String idleText, float x, float y) {
        if (idleText == null) {
            return;
        }
        DIFontManager.axiforma18.drawString(idleText, Math.round(x + 8.0f), Math.round(y + 8.0f), 0xFFFFFFFF);
    }

    private String getSessionName(Minecraft mc) {
        try {
            return mc.getSession() != null ? mc.getSession().getUsername() : "Player";
        } catch (Throwable ignored) {
            return "Player";
        }
    }

    private void drawEntry(ToggleEntry entry, float y, long now) {
        long elapsed = now - entry.createdAt;
        float fade = 1.0f;
        if (elapsed > durationMs) {
            fade = Math.max(0.0f, 1.0f - (float) (elapsed - durationMs) / (float) EXIT_FADE_MS);
        }

        double switchValue = entry.animation.getOutput();

        int trackColor = argb(
                Math.round(255.0f * fade),
                (int) (128 - 80 * switchValue),
                (int) (128 - 11 * switchValue),
                (int) (128 + 19 * switchValue)
        );
        int white = argb(Math.round(255.0f * fade), 255, 255, 255);
        int dark = argb(Math.round(255.0f * fade), 64, 64, 64);
        float x = renderX;

        DIRoundedUtils.drawRoundedRect(x + 6.0f, y + 13.0f, 26.0f, 14.0f, 6.5f, dark);
        DIRoundedUtils.drawRoundedRect(x + 7.0f, y + 14.0f, 24.0f, 12.0f, 6.0f, trackColor);
        DIRoundedUtils.drawRoundedRect(x + 8.0f + 12.0f * (float) switchValue, y + 15.0f, 10.0f, 10.0f, 4.5f, argb(Math.round(255.0f * fade), 43, 43, 43));

        String desc = entry.enabled
                ? entry.moduleName + " has been \u00A72Enabled"
                : entry.moduleName + " has been \u00A74Disabled";
        DIFontManager.axiformaBold20.drawString(TITLE, Math.round(x + 38.0f), Math.round(y + 9.0f), white);
        DIFontManager.axiforma18.drawString(desc, Math.round(x + 38.0f), Math.round(y + 22.0f), white);
    }

    private static int argb(int a, int r, int g, int b) {
        int aa = Math.max(0, Math.min(255, a));
        int rr = Math.max(0, Math.min(255, r));
        int gg = Math.max(0, Math.min(255, g));
        int bb = Math.max(0, Math.min(255, b));
        return (aa << 24) | (rr << 16) | (gg << 8) | bb;
    }

    private static final class ToggleEntry {
        private String moduleName;
        private boolean enabled;
        private long createdAt;
        private final DIEaseOutExpo animation;

        private ToggleEntry(String moduleName, boolean enabled, long createdAt) {
            this.moduleName = moduleName;
            this.enabled = enabled;
            this.createdAt = createdAt;
            this.animation = new DIEaseOutExpo(300, 1,
                    enabled ? DIDirection.FORWARDS : DIDirection.BACKWARDS);
        }

        private void resetTime(long now, boolean newEnabled) {
            this.createdAt = now;
            this.enabled = newEnabled;
            this.animation.setDirection(newEnabled ? DIDirection.FORWARDS : DIDirection.BACKWARDS);
        }
    }
}
