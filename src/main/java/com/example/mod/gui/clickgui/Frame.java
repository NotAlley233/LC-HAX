package com.example.mod.gui.clickgui;

import com.example.mod.gui.clickgui.entry.ModuleEntry;
import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.module.Category;
import com.example.mod.module.Module;
import com.example.mod.property.Property;
import com.example.mod.property.PropertyManager;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;
import com.example.mod.util.render.ShadowUtil;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private final Category category;
    private final List<ModuleEntry> entries = new ArrayList<>();
    private float x;
    private float y;
    private final float width;
    private final float headerHeight;
    private boolean expanded = true;
    private boolean dragging;
    private float dragX;
    private float dragY;
    private float currentHeight;

    public Frame(Category category, float x, float y, float width, float headerHeight, List<Module> modules, PropertyManager propertyManager) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.headerHeight = headerHeight;
        this.currentHeight = headerHeight;
        for (Module module : modules) {
            List<Property<?>> properties = propertyManager.getProperties(module);
            entries.add(new ModuleEntry(module, properties));
        }
    }

    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        if (dragging) {
            this.x = mouseX - dragX;
            this.y = mouseY - dragY + scrollOffset;
        }
        float ry = y - scrollOffset;
        float target = headerHeight;
        if (expanded) {
            for (ModuleEntry entry : entries) {
                target += entry.getCurrentHeight();
            }
        }
        currentHeight = AnimationUtil.lerp(currentHeight, target, 0.2f);

        int alpha = (int) (255 * alphaProgress);
        int shadowColor = new java.awt.Color(0, 0, 0, Math.min(120, (int) (alpha * 0.5f))).getRGB();
        ShadowUtil.drawShadow(x, ry, width, currentHeight, MaterialTheme.CORNER_RADIUS_FRAME, 12f, shadowColor);
        int frameBgColor = new java.awt.Color(15, 15, 15, alpha).getRGB();
        RoundedUtils.drawRoundedRect(x, ry, width, headerHeight, MaterialTheme.CORNER_RADIUS_FRAME, frameBgColor, true, true, currentHeight <= headerHeight + 0.5f, currentHeight <= headerHeight + 0.5f);
        if (currentHeight > headerHeight) {
            int listBgColor = new java.awt.Color(28, 28, 28, alpha).getRGB();
            RoundedUtils.drawRoundedRect(x, ry + headerHeight, width, currentHeight - headerHeight, MaterialTheme.CORNER_RADIUS_FRAME, listBgColor, false, false, true, true);
        }

        RenderUtil.drawString(category.getName(), x + 8, ry + 6, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));
        RenderUtil.drawString(expanded ? "-" : "+", x + width - 9, ry + 6, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));

        if (currentHeight > headerHeight) {
            RenderUtil.scissor(x, ry + headerHeight, width, currentHeight - headerHeight);
            float cy = y + headerHeight;
            for (ModuleEntry entry : entries) {
                entry.setBounds(x, cy, width);
                entry.draw(mouseX, mouseY, partialTicks, scrollOffset, alphaProgress);
                cy += entry.getCurrentHeight();
            }
            RenderUtil.releaseScissor();
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        float ry = y - scrollOffset;
        if (mouseX >= x && mouseX <= x + width && mouseY >= ry && mouseY <= ry + headerHeight) {
            if (mouseButton == 0) {
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - ry;
                return true;
            }
            if (mouseButton == 1) {
                expanded = !expanded;
                return true;
            }
        }
        if (expanded) {
            for (ModuleEntry entry : entries) {
                entry.mouseClicked(mouseX, mouseY, mouseButton, scrollOffset);
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state, int scrollOffset) {
        if (state == 0) dragging = false;
        if (expanded) {
            for (ModuleEntry entry : entries) {
                entry.mouseReleased(mouseX, mouseY, state, scrollOffset);
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!expanded) return;
        for (ModuleEntry entry : entries) {
            entry.keyTyped(typedChar, keyCode);
        }
    }

    public float getBottom() {
        return y + currentHeight;
    }
}
