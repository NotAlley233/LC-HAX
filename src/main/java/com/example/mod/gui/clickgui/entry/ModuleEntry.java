package com.example.mod.gui.clickgui.entry;

import com.example.mod.gui.clickgui.component.DropdownComponent;
import com.example.mod.gui.clickgui.component.KeybindComponent;
import com.example.mod.gui.clickgui.component.NumberSliderComponent;
import com.example.mod.gui.clickgui.component.SliderComponent;
import com.example.mod.gui.clickgui.component.SwitchComponent;
import com.example.mod.gui.clickgui.component.UiComponent;
import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.module.KeyBindingManager;
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
    private float x, y, width;
    private final float rowHeight = 28f;
    private boolean expanded;
    private float hoverAnim;
    private float enableAnim;
    private float settingsHeight;

    public ModuleEntry(Module module, List<Property<?>> properties, KeyBindingManager keyBindingManager) {
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
        if (keyBindingManager != null) {
            final String modName = module.name();
            components.add(new KeybindComponent(
                    "Keybind",
                    () -> {
                        Integer key = keyBindingManager.getBoundKey(modName);
                        return key != null ? key : 0;
                    },
                    (keyCode) -> {
                        if (keyCode == 0) {
                            keyBindingManager.unbind(modName);
                        } else {
                            keyBindingManager.bind(modName, keyCode, true);
                        }
                    }
            ));
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

        enableAnim = AnimationUtil.lerp(enableAnim, module.enabled() ? 1f : 0f, 0.15f);

        float settingsTarget = expanded ? getVisibleContentHeight() : 0f;
        settingsHeight = AnimationUtil.lerp(settingsHeight, settingsTarget, 0.2f);
        if (Math.abs(settingsHeight - settingsTarget) < 0.5f) settingsHeight = settingsTarget;

        if (enableAnim > 0.01f) {
            int accentAlpha = (int) (alpha * enableAnim);
            RoundedUtils.drawRoundedRect(x + 2, ry + 7, 3, rowHeight - 14, 1.5f,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, accentAlpha));
        }

        int nameColor = MaterialTheme.blendColor(
                MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha),
                MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha),
                enableAnim);
        RenderUtil.drawString(module.name(), x + 10, ry + 5, nameColor);

        String desc = module.description();
        if (desc != null && !desc.isEmpty()) {
            RenderUtil.drawString(desc, x + 10, ry + 16,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, (int) (alpha * 0.45f)));
        }

        if (!components.isEmpty()) {
            String indicator = expanded ? "-" : "+";
            RenderUtil.drawString(indicator, x + width - 14, ry + 9,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha));
        }

        if (settingsHeight > 1f) {
            float settingsY = ry + rowHeight;
            RoundedUtils.drawRoundedRect(x + 6, settingsY, width - 12, settingsHeight,
                    MaterialTheme.CORNER_RADIUS_ITEM,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.SETTINGS_BG, alpha));

            RenderUtil.scissor(x + 6, settingsY, width - 12, settingsHeight);
            float cy = y + rowHeight + 2;
            for (UiComponent component : components) {
                if (!component.isVisible()) continue;
                component.setBounds(x + 10, cy, width - 20);
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
        if (expanded && settingsHeight > 1f) {
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

    public boolean isListeningForKey() {
        if (!expanded) return false;
        for (UiComponent component : components) {
            if (component instanceof KeybindComponent && ((KeybindComponent) component).isBinding()) {
                return true;
            }
        }
        return false;
    }

    public String getModuleName() {
        return module.name();
    }

    public float getY() {
        return y;
    }

    public float getCurrentHeight() {
        return rowHeight + settingsHeight;
    }

    private float getVisibleContentHeight() {
        float h = 4f;
        for (UiComponent component : components) {
            if (component.isVisible()) h += component.getHeight();
        }
        return h;
    }
}
