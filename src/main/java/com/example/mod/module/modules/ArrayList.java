package com.example.mod.module.modules;

import com.example.mod.ModContext;
import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.OverlayRenderable;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.Color;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ArrayList extends BaseModule implements OverlayRenderable {

    private static final int ROW_HEIGHT = 12;
    private static final int PADDING_H = 4;
    private static final int ACCENT_WIDTH = 2;
    private static final float ANIM_SPEED = 0.15f;

    private final Map<String, Float> slideProgress = new HashMap<>();

    private String colorMode = "Static";
    private int bgOpacity = 120;
    private int yOffset = 2;

    public ArrayList() {
        super("arraylist", "Displays enabled modules on screen.", Category.RENDER, false);
    }

    public String getColorMode() { return colorMode; }
    public void setColorMode(String colorMode) { this.colorMode = colorMode; }

    public int getBgOpacity() { return bgOpacity; }
    public void setBgOpacity(int bgOpacity) { this.bgOpacity = Math.max(0, Math.min(255, bgOpacity)); }

    public int getYOffset() { return yOffset; }
    public void setYOffset(int yOffset) { this.yOffset = Math.max(0, Math.min(500, yOffset)); }

    @Override
    public void onRenderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) return;

        ModuleManager moduleManager = ModContext.getModuleManager();
        if (moduleManager == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();

        java.util.List<Module> allModules = moduleManager.all();
        java.util.List<Module> sorted = new java.util.ArrayList<>(allModules);
        sorted.sort(Comparator.comparingInt((Module m) -> RenderUtil.getStringWidth(m.name())).reversed());

        int y = yOffset;
        int visibleIndex = 0;

        for (Module module : sorted) {
            if (module == this) continue;

            String name = module.name();
            boolean active = module.enabled();

            float current = slideProgress.getOrDefault(name, 0f);
            float target = active ? 1f : 0f;
            float next = AnimationUtil.lerp(current, target, ANIM_SPEED);

            if (Math.abs(next - target) < 0.005f) next = target;
            slideProgress.put(name, next);

            if (next <= 0.001f) continue;

            int textWidth = RenderUtil.getStringWidth(name);
            int entryWidth = textWidth + PADDING_H * 2 + ACCENT_WIDTH;

            float slideOffset = (1f - next) * (entryWidth + 2);
            float x = screenWidth - entryWidth + slideOffset;

            int bgColor = (bgOpacity << 24) | 0x000000;
            RenderUtil.drawRect(x, y, entryWidth, ROW_HEIGHT, bgColor);

            int accentColor = getColor(module, visibleIndex);
            RenderUtil.drawRect(x, y, ACCENT_WIDTH, ROW_HEIGHT, accentColor);

            RenderUtil.drawString(name, x + ACCENT_WIDTH + PADDING_H, y + 2, 0xFFFFFFFF);

            y += ROW_HEIGHT;
            visibleIndex++;
        }
    }

    private int getColor(Module module, int index) {
        switch (colorMode) {
            case "Rainbow":
                return Color.HSBtoRGB((System.currentTimeMillis() % 3000L) / 3000f - index * 0.05f, 0.7f, 1f);
            case "Category":
                return getCategoryColor(module.category());
            case "Static":
            default:
                return 0xFFFFFFFF;
        }
    }

    private static int getCategoryColor(Category category) {
        switch (category) {
            case COMBAT:   return 0xFFFF5555;
            case MOVEMENT: return 0xFF55FF55;
            case PLAYER:   return 0xFF55FFFF;
            case RENDER:   return 0xFFFF55FF;
            case UTILITY:  return 0xFFFFFF55;
            case ADVANCED: return 0xFFFFAA00;
            default:       return 0xFFFFFFFF;
        }
    }
}
