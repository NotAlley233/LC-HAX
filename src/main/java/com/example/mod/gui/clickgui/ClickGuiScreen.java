package com.example.mod.gui.clickgui;

import com.example.mod.gui.clickgui.entry.ModuleEntry;
import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.module.Category;
import com.example.mod.module.KeyBindingManager;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.modules.ClickGUIModule;
import com.example.mod.property.Property;
import com.example.mod.property.PropertyManager;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.GlowRenderer;
import com.example.mod.util.render.GuiAnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;
import com.example.mod.util.render.ShadowUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClickGuiScreen extends GuiScreen {

    private static final float PANEL_GLOW_BLUR_RADIUS = 14.0f;
    private static final float PANEL_GLOW_EXPOSURE = 2.5f;

    private final ModuleManager moduleManager;

    private final Map<Category, List<ModuleEntry>> modulesByCategory = new LinkedHashMap<>();
    private final List<Category> categories = new ArrayList<>();
    private Category selectedCategory;
    private final Map<Category, float[]> scrollState = new HashMap<>();

    private String searchQuery = "";
    private boolean searchFocused = false;
    private final List<ModuleEntry> searchResults = new ArrayList<>();
    private final float[] searchScroll = {0, 0, 0};

    private boolean closing = false;
    private long animationStart = 0L;
    private float sidebarIndicatorY = -1;

    private static final long ANIM_MS = 300L;
    private static final int HEADER_HEIGHT = 30;
    private static final int TAB_HEIGHT = 28;
    private static final float FRICTION = 0.85f;
    private static final float SNAP_STRENGTH = 0.15f;

    private int panelW, panelH, panelX, panelY;
    private int sidebarW, contentX, contentY, contentW, contentH;

    public ClickGuiScreen(ModuleManager moduleManager, PropertyManager propertyManager, KeyBindingManager keyBindingManager) {
        this.moduleManager = moduleManager;
        for (Category category : Category.values()) {
            List<Module> modules = new ArrayList<>();
            for (Module module : moduleManager.all()) {
                if (module.category() == category) modules.add(module);
            }
            modules.sort(Comparator.comparing(Module::name, String.CASE_INSENSITIVE_ORDER));
            if (!modules.isEmpty()) {
                categories.add(category);
                List<ModuleEntry> entries = new ArrayList<>();
                for (Module module : modules) {
                    List<Property<?>> props = propertyManager.getProperties(module);
                    entries.add(new ModuleEntry(module, props, keyBindingManager));
                }
                modulesByCategory.put(category, entries);
                scrollState.put(category, new float[]{0, 0, 0});
            }
        }
        if (!categories.isEmpty()) {
            selectedCategory = categories.get(0);
        }
    }

    private void updateLayout() {
        panelW = Math.min(480, this.width - 10);
        panelH = Math.min(340, this.height - 10);
        panelX = (this.width - panelW) / 2;
        panelY = (this.height - panelH) / 2;
        sidebarW = (panelW >= 350) ? 90 : (int) (panelW * 0.25f);
        contentX = panelX + sidebarW;
        contentY = panelY + HEADER_HEIGHT;
        contentW = panelW - sidebarW;
        contentH = panelH - HEADER_HEIGHT;
    }

    private boolean shouldDrawPanelGlow() {
        Module m = moduleManager.get("clickgui");
        return m instanceof ClickGUIModule && ((ClickGUIModule) m).isPanelGlow();
    }

    @Override
    public void initGui() {
        super.initGui();
        animationStart = System.currentTimeMillis();
        closing = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float progress = animationProgress();
        if (progress <= 0) return;

        updateLayout();
        int alpha = closing ? Math.max(0, (int) (255 * progress)) : 255;
        float overlayAlpha = closing ? 0.55f * progress : 0.55f;

        if (shouldDrawPanelGlow() && progress > 0.02f) {
            java.awt.Color primary = MaterialTheme.PRIMARY_COLOR;
            float pr = primary.getRed() / 255.0f;
            float pg = primary.getGreen() / 255.0f;
            float pb = primary.getBlue() / 255.0f;
            final float px = panelX;
            final float py = panelY;
            final float pw = panelW;
            final float ph = panelH;
            float exp = PANEL_GLOW_EXPOSURE * progress;
            GlowRenderer.drawOuterGlow(
                    () -> RoundedUtils.drawRoundedRect(
                            px, py, pw, ph,
                            MaterialTheme.CORNER_RADIUS_PANEL,
                            0xFFFFFFFF),
                    PANEL_GLOW_BLUR_RADIUS,
                    Math.max(0.15f, exp),
                    pr, pg, pb);
        }

        drawRect(0, 0, this.width, this.height, RenderUtil.applyOpacity(0xFF08080C, overlayAlpha));

        ShadowUtil.drawShadow(panelX, panelY, panelW, panelH,
                MaterialTheme.CORNER_RADIUS_PANEL, 22f,
                new java.awt.Color(0, 0, 0, (int) (110 * progress)).getRGB());

        RoundedUtils.drawRoundedRect(panelX, panelY, panelW, panelH,
                MaterialTheme.CORNER_RADIUS_PANEL,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.PANEL_BG, alpha));

        RoundedUtils.drawRoundedRect(panelX, panelY, panelW, HEADER_HEIGHT,
                MaterialTheme.CORNER_RADIUS_PANEL,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.HEADER_BG, alpha),
                true, true, false, false);

        RenderUtil.drawString("LC-HAX", panelX + 14, panelY + 10,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha));

        drawSearchBox(alpha);

        RenderUtil.drawRect(panelX + 4, panelY + HEADER_HEIGHT - 1, panelW - 8, 1,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.DIVIDER, alpha));

        RoundedUtils.drawRoundedRect(panelX, contentY, sidebarW, contentH,
                MaterialTheme.CORNER_RADIUS_PANEL,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.SIDEBAR_BG, alpha),
                false, false, true, false);

        drawSidebar(mouseX, mouseY, alpha);

        RenderUtil.drawRect(contentX - 1, contentY + 4, 1, contentH - 8,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.DIVIDER, alpha));

        drawContent(mouseX, mouseY, partialTicks, progress);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawSearchBox(int alpha) {
        float searchX = panelX + panelW - 152;
        float searchY = panelY + 7;
        float searchW = Math.min(140, panelW - 100);
        float searchH = 16;
        if (searchW < 40) return;

        int bgColor = searchFocused
                ? MaterialTheme.getRGBWithAlpha(MaterialTheme.SEARCH_BG, Math.min(255, alpha + 20))
                : MaterialTheme.getRGBWithAlpha(MaterialTheme.SEARCH_BG, alpha);
        RoundedUtils.drawRoundedRect(searchX, searchY, searchW, searchH,
                MaterialTheme.CORNER_RADIUS_SEARCH, bgColor);

        if (searchFocused) {
            RoundedUtils.drawRoundedRect(searchX - 0.5f, searchY - 0.5f, searchW + 1, searchH + 1,
                    MaterialTheme.CORNER_RADIUS_SEARCH + 0.5f,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, (int) (alpha * 0.3f)));
            RoundedUtils.drawRoundedRect(searchX, searchY, searchW, searchH,
                    MaterialTheme.CORNER_RADIUS_SEARCH, bgColor);
        }

        String display = searchQuery.isEmpty() && !searchFocused ? "Search..." : searchQuery + (searchFocused ? "_" : "");
        int textColor = searchQuery.isEmpty() && !searchFocused
                ? MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, (int) (alpha * 0.5f))
                : MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha);
        RenderUtil.drawString(display, searchX + 6, searchY + 4, textColor);
    }

    private void drawSidebar(int mouseX, int mouseY, int alpha) {
        float targetIndicatorY = contentY + 6;
        if (searchQuery.isEmpty()) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i) == selectedCategory) {
                    targetIndicatorY = contentY + 6 + i * TAB_HEIGHT;
                    break;
                }
            }
            if (sidebarIndicatorY < 0) sidebarIndicatorY = targetIndicatorY;
            sidebarIndicatorY = AnimationUtil.lerp(sidebarIndicatorY, targetIndicatorY, 0.18f);

            RoundedUtils.drawRoundedRect(panelX + 4, sidebarIndicatorY, sidebarW - 8, TAB_HEIGHT,
                    MaterialTheme.CORNER_RADIUS_TAB,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.ACTIVE_TAB, alpha));
            RoundedUtils.drawRoundedRect(panelX + 5, sidebarIndicatorY + 6, 3, TAB_HEIGHT - 12, 1.5f,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha));
        }

        for (int i = 0; i < categories.size(); i++) {
            Category cat = categories.get(i);
            float tabY = contentY + 6 + i * TAB_HEIGHT;
            boolean isSelected = cat == selectedCategory && searchQuery.isEmpty();

            boolean hovered = searchQuery.isEmpty()
                    && mouseX >= panelX + 4
                    && mouseX <= panelX + sidebarW - 4
                    && mouseY >= tabY
                    && mouseY <= tabY + TAB_HEIGHT;
            if (hovered && !isSelected) {
                RoundedUtils.drawRoundedRect(panelX + 4, tabY, sidebarW - 8, TAB_HEIGHT,
                        MaterialTheme.CORNER_RADIUS_TAB,
                        MaterialTheme.getRGBWithAlpha(MaterialTheme.DROPDOWN_HOVER, alpha));
            }

            int textColor = isSelected
                    ? 0xFFFFFFFF
                    : MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha);
            RenderUtil.drawString(cat.getName(), panelX + 16, tabY + 9, textColor);
        }
    }

    private void drawContent(int mouseX, int mouseY, float partialTicks, float progress) {
        List<ModuleEntry> entries = getVisibleEntries();
        float[] scroll = getActiveScroll();
        updateScroll(scroll, entries, contentH);
        layoutEntries(entries, scroll[0]);
        boolean mouseInContent = mouseX >= contentX && mouseX <= contentX + contentW
                && mouseY >= contentY && mouseY <= contentY + contentH;
        int hitMouseX = mouseInContent ? mouseX : Integer.MIN_VALUE;
        int hitMouseY = mouseInContent ? mouseY : Integer.MIN_VALUE;

        RenderUtil.scissor(contentX, contentY, contentW, contentH);

        for (ModuleEntry entry : entries) {
            float entryY = entry.getY();
            if (entryY + entry.getCurrentHeight() >= contentY - 20 && entryY <= contentY + contentH + 20) {
                entry.draw(hitMouseX, hitMouseY, partialTicks, 0, progress);
            }
        }

        RenderUtil.releaseScissor();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        updateLayout();

        float searchX = panelX + panelW - 152;
        float searchY = panelY + 7;
        float searchW = Math.min(140, panelW - 100);
        if (searchW >= 40 && mouseButton == 0 && mouseX >= searchX && mouseX <= searchX + searchW
                && mouseY >= searchY && mouseY <= searchY + 16) {
            searchFocused = true;
            return;
        }
        searchFocused = false;

        if (searchQuery.isEmpty() && mouseX >= panelX + 4 && mouseX <= panelX + sidebarW - 4
                && mouseY >= contentY && mouseY <= contentY + contentH) {
            for (int i = 0; i < categories.size(); i++) {
                float tabY = contentY + 6 + i * TAB_HEIGHT;
                if (mouseY >= tabY && mouseY <= tabY + TAB_HEIGHT) {
                    selectedCategory = categories.get(i);
                    return;
                }
            }
        }

        if (mouseX >= contentX && mouseX <= contentX + contentW
                && mouseY >= contentY && mouseY <= contentY + contentH) {
            List<ModuleEntry> entries = getVisibleEntries();
            float[] scroll = getActiveScroll();
            layoutEntries(entries, scroll != null ? scroll[0] : 0f);
            for (ModuleEntry entry : entries) {
                entry.mouseClicked(mouseX, mouseY, mouseButton, 0);
            }
        }

        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (Exception ignored) {}
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        List<ModuleEntry> entries = getVisibleEntries();
        for (ModuleEntry entry : entries) {
            entry.mouseReleased(mouseX, mouseY, state, 0);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        List<ModuleEntry> visible = getVisibleEntries();
        for (ModuleEntry entry : visible) {
            if (entry.isListeningForKey()) {
                for (ModuleEntry e : visible) e.keyTyped(typedChar, keyCode);
                return;
            }
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (searchFocused && !searchQuery.isEmpty()) {
                searchQuery = "";
                searchFocused = false;
                return;
            }
            mc.displayGuiScreen(null);
            return;
        }

        if (searchFocused) {
            if (keyCode == Keyboard.KEY_BACK) {
                if (!searchQuery.isEmpty()) {
                    searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                }
            } else if (typedChar >= ' ' && typedChar < 127) {
                searchQuery += typedChar;
            }
            return;
        }

        for (ModuleEntry entry : visible) {
            entry.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            float[] scroll = getActiveScroll();
            if (scroll != null) {
                scroll[2] += wheel > 0 ? -25 : 25;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private List<ModuleEntry> getVisibleEntries() {
        if (!searchQuery.isEmpty()) {
            searchResults.clear();
            String query = searchQuery.toLowerCase();
            for (List<ModuleEntry> entries : modulesByCategory.values()) {
                for (ModuleEntry entry : entries) {
                    if (entry.getModuleName().toLowerCase().contains(query)) {
                        searchResults.add(entry);
                    }
                }
            }
            return searchResults;
        }
        List<ModuleEntry> entries = modulesByCategory.get(selectedCategory);
        return entries != null ? entries : Collections.<ModuleEntry>emptyList();
    }

    private float[] getActiveScroll() {
        if (!searchQuery.isEmpty()) return searchScroll;
        return scrollState.get(selectedCategory);
    }

    private void updateScroll(float[] scroll, List<ModuleEntry> entries, int visibleH) {
        if (scroll == null) return;
        scroll[1] += scroll[2];
        scroll[2] *= FRICTION;

        float totalHeight = 8;
        for (ModuleEntry entry : entries) {
            totalHeight += entry.getCurrentHeight();
        }
        float maxScroll = Math.max(0, totalHeight - visibleH);
        scroll[1] = Math.max(0, Math.min(maxScroll, scroll[1]));
        scroll[0] += (scroll[1] - scroll[0]) * SNAP_STRENGTH;

        if (Math.abs(scroll[1] - scroll[0]) < 0.5f && Math.abs(scroll[2]) < 0.5f) {
            scroll[0] = scroll[1];
            scroll[2] = 0;
        }
    }

    private void layoutEntries(List<ModuleEntry> entries, float displayScroll) {
        float cy = contentY + 4 - displayScroll;
        for (ModuleEntry entry : entries) {
            entry.setBounds(contentX + 6, cy, contentW - 12);
            cy += entry.getCurrentHeight();
        }
    }

    private float animationProgress() {
        float t = GuiAnimationUtil.clamp01((System.currentTimeMillis() - animationStart) / (float) ANIM_MS);
        float eased = GuiAnimationUtil.easeOutCubic(t);
        if (closing) {
            float r = 1f - eased;
            if (r <= 0.03f) {
                closing = false;
                mc.displayGuiScreen(null);
                return 0f;
            }
            return r;
        }
        return eased;
    }
}
