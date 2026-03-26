package com.example.mod.gui.components.settings;

import com.example.mod.gui.components.Component;
import com.example.mod.property.properties.IntProperty;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;
import java.awt.Color;

public class SliderComponent extends Component {
    private final IntProperty property;
    private boolean dragging = false;
    private float renderValue = 0f;

    public SliderComponent(IntProperty property) {
        super(0, 0, 0, 16);
        this.property = property;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Handle dragging
        if (dragging) {
            float percent = Math.min(1, Math.max(0, (mouseX - x) / width));
            // Assuming default property min=0, max=100 for now. Needs real min/max if property supports it.
            // But currently IntProperty just uses get/set without limits in its standard interface.
            // We'll map it 0-20 for demonstration, or read min/max if added.
            int min = 0, max = 20; // Default arbitrary limit
            int newValue = (int) (min + (max - min) * percent);
            property.setValue(newValue);
        }

        // Draw background
        RoundedUtils.drawRoundedRect(x, y, width, height, 0f, new Color(30, 30, 30, 180).getRGB());
        
        RenderUtil.drawString(property.getName() + ": " + property.getValue(), x + 6, y + 2, 0xFFDDDDDD);

        // Draw slider track
        RoundedUtils.drawRoundedRect(x + 6, y + 12, width - 12, 2, 1f, 0xFF151515);

        // Assume 0-20 scale for animation
        float targetPercent = (property.getValue() - 0f) / 20f;
        targetPercent = Math.min(1, Math.max(0, targetPercent));
        renderValue = AnimationUtil.lerp(renderValue, targetPercent, 0.2f);

        // Draw slider fill
        RoundedUtils.drawRoundedRect(x + 6, y + 12, (width - 12) * renderValue, 2, 1f, 0xFF00AA00);
        
        // Draw slider thumb
        float thumbX = x + 6 + (width - 12) * renderValue;
        RoundedUtils.drawRoundedRect(thumbX - 2, y + 10, 4, 6, 2f, 0xFFEEEEEE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            dragging = false;
        }
    }
}
