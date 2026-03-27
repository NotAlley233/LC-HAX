package com.example.mod.gui.clickgui.entry;

import com.example.mod.gui.clickgui.component.DropdownComponent;
import com.example.mod.gui.clickgui.component.NumberSliderComponent;
import com.example.mod.gui.clickgui.component.SliderComponent;
import com.example.mod.gui.clickgui.component.SwitchComponent;
import com.example.mod.gui.clickgui.component.UiComponent;
import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.module.Module;
import com.example.mod.property.Property;
import com.example.mod.property.properties.BooleanProperty;
import com.example.mod.property.properties.FloatProperty;
import com.example.mod.property.properties.IntProperty;
import com.example.mod.property.properties.ModeProperty;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;

import java.util.ArrayList;
import java.util.List;

public class ModuleEntry {
    private final Module module;
    private final List<UiComponent> components = new ArrayList<>();
    private float x;
    private float y;
    private float width;
    private final float rowHeight = 22f;
    private boolean expanded;
    private float hoverAnim;
    private float settingsHeight;

    public ModuleEntry(Module module, List<Property<?>> properties) {
        this.module = module;
        if (properties != null) {
            for (Property<?> property : properties) {
                if (!property.isVisible() || "enabled".equalsIgnoreCase(property.getName())) continue;
                if (property instanceof BooleanProperty) {
                    components.add(new SwitchComponent((BooleanProperty) property));
                } else if (property instanceof IntProperty) {
                    components.add(new SliderComponent((IntProperty) property));
                } else if (property instanceof FloatProperty) {
                    components.add(new NumberSliderComponent((FloatProperty) property));
                } else if (property instanceof ModeProperty) {
                    components.add(new DropdownComponent((ModeProperty) property));
                }
            }
        }
    }

    public void setBounds(float x, float y, float width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= ry && mouseY <= ry + rowHeight;
        hoverAnim = AnimationUtil.lerp(hoverAnim, hovered ? 1f : 0f, 0.25f);
        float settingsTarget = expanded ? getVisibleContentHeight() : 0f;
        settingsHeight = AnimationUtil.lerp(settingsHeight, settingsTarget, 0.24f);

        int hoverColor = MaterialTheme.getRGBWithAlpha(MaterialTheme.SURFACE_CONTAINER_HIGH, (int) (alpha * hoverAnim));
        RoundedUtils.drawRoundedRect(x + 2, ry, width - 4, rowHeight, MaterialTheme.CORNER_RADIUS_ITEM, hoverColor);
        int textColor = MaterialTheme.getRGBWithAlpha(module.enabled() ? MaterialTheme.PRIMARY_COLOR : MaterialTheme.TEXT_COLOR, alpha);
        RenderUtil.drawString(module.name(), x + 9, ry + 7, textColor);
        if (module.enabled()) {
            RoundedUtils.drawRoundedRect(x + 4, ry + 9, 3, 3, 1.5f, MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha));
        }
        if (!components.isEmpty()) {
            RenderUtil.drawString(expanded ? "..." : ":", x + width - 12, ry + 7, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha));
        }

        if (settingsHeight > 1f) {
            float contentY = ry + rowHeight;
            RoundedUtils.drawRoundedRect(x + 2, contentY, width - 4, settingsHeight, MaterialTheme.CORNER_RADIUS_ITEM, new java.awt.Color(10, 10, 12, (int) (100 * alphaProgress)).getRGB());
            RenderUtil.scissor(x + 2, contentY, width - 4, settingsHeight);
            float cy = y + rowHeight;
            for (UiComponent component : components) {
                if (!component.isVisible()) continue;
                component.setBounds(x + 4, cy, width - 8);
                component.draw(mouseX, mouseY, partialTicks, scrollOffset, alphaProgress);
                cy += component.getHeight();
            }
            RenderUtil.releaseScissor();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        float ry = y - scrollOffset;
        if (mouseX >= x && mouseX <= x + width && mouseY >= ry && mouseY <= ry + rowHeight) {
            if (mouseButton == 0) {
                module.setEnabled(!module.enabled());
            } else if (mouseButton == 1 && !components.isEmpty()) {
                expanded = !expanded;
            }
            return;
        }
        if (expanded) {
            for (UiComponent component : components) {
                component.mouseClicked(mouseX, mouseY, mouseButton, scrollOffset);
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state, int scrollOffset) {
        if (!expanded) return;
        for (UiComponent component : components) {
            component.mouseReleased(mouseX, mouseY, state, scrollOffset);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!expanded) return;
        for (UiComponent component : components) {
            component.keyTyped(typedChar, keyCode);
        }
    }

    public float getCurrentHeight() {
        return rowHeight + settingsHeight;
    }

    private float getVisibleContentHeight() {
        float h = 0f;
        for (UiComponent component : components) {
            if (component.isVisible()) {
                h += component.getHeight();
            }
        }
        return h;
    }
}
