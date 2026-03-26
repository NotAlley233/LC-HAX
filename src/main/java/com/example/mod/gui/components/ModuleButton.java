package com.example.mod.gui.components;

import com.example.mod.module.Module;
import com.example.mod.property.Property;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class ModuleButton extends Component {
    private final Module module;
    private final List<Component> settings = new ArrayList<>();
    private boolean expanded = false;
    
    // Animations
    private float toggleAnim = 0f;
    private float hoverAnim = 0f;
    private float currentHeight = 16f;

    public ModuleButton(Module module) {
        super(0, 0, 0, 16);
        this.module = module;
    }

    public void addSetting(Component setting) {
        this.settings.add(setting);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean hovered = isHovered(mouseX, mouseY);
        
        // Update animations
        hoverAnim = AnimationUtil.lerp(hoverAnim, hovered ? 1f : 0f, 0.15f);
        toggleAnim = AnimationUtil.lerp(toggleAnim, module.enabled() ? 1f : 0f, 0.15f);

        // Calculate target height
        float targetHeight = 16;
        if (expanded) {
            for (Component c : settings) {
                targetHeight += c.getHeight();
            }
        }
        currentHeight = AnimationUtil.lerp(currentHeight, targetHeight, 0.2f);
        this.height = currentHeight;

        // Draw background
        int baseR = 40, baseG = 40, baseB = 40;
        int hoverR = 60, hoverG = 60, hoverB = 60;
        int activeR = 0, activeG = 170, activeB = 0;
        
        // Blend colors based on hover and toggle state
        float r = baseR + (hoverR - baseR) * hoverAnim;
        float g = baseG + (hoverG - baseG) * hoverAnim;
        float b = baseB + (hoverB - baseB) * hoverAnim;
        
        r = r + (activeR - r) * toggleAnim;
        g = g + (activeG - g) * toggleAnim;
        b = b + (activeB - b) * toggleAnim;
        
        int bgColor = new Color((int)r, (int)g, (int)b, 255).getRGB();

        // Draw the button background with slight margin
        RoundedUtils.drawRoundedRect(x + 2, y + 1, width - 4, 14, 3f, bgColor);

        // Draw module name
        RenderUtil.drawString(module.name(), x + 8, y + 4, 0xFFFFFFFF);

        // Draw settings if expanded
        if (expanded && currentHeight > 16) {
            float currentY = y + 16;
            for (Component setting : settings) {
                setting.setX(this.x + 2); // Indent settings slightly
                setting.setY(currentY);
                setting.setWidth(this.width - 4);
                setting.drawScreen(mouseX, mouseY, partialTicks);
                currentY += setting.getHeight();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHoveredButton(mouseX, mouseY)) {
            if (mouseButton == 0) {
                module.setEnabled(!module.enabled());
            } else if (mouseButton == 1 && !settings.isEmpty()) {
                expanded = !expanded;
            }
        }

        if (expanded) {
            for (Component setting : settings) {
                setting.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (expanded) {
            for (Component setting : settings) {
                setting.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (expanded) {
            for (Component setting : settings) {
                setting.keyTyped(typedChar, keyCode);
            }
        }
    }

    private boolean isHoveredButton(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 16;
    }
}
