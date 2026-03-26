package com.example.mod.gui.components.settings;

import com.example.mod.gui.components.Component;
import com.example.mod.property.properties.BooleanProperty;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;
import java.awt.Color;

public class BooleanComponent extends Component {
    private final BooleanProperty property;
    private float anim = 0f;

    public BooleanComponent(BooleanProperty property) {
        super(0, 0, 0, 14);
        this.property = property;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        anim = AnimationUtil.lerp(anim, property.getValue() ? 1f : 0f, 0.2f);

        // Background
        RoundedUtils.drawRoundedRect(x, y, width, height, 0f, new Color(30, 30, 30, 180).getRGB());
        
        RenderUtil.drawString(property.getName(), x + 6, y + 3, 0xFFDDDDDD);

        // Checkbox Background
        float boxX = x + width - 14;
        RoundedUtils.drawRoundedRect(boxX, y + 2, 10, 10, 2f, 0xFF151515);
        
        // Checkbox Fill
        if (anim > 0.05f) {
            float offset = (1f - anim) * 4f; // scale down towards center
            RoundedUtils.drawRoundedRect(boxX + 1 + offset, y + 3 + offset, 8 * anim, 8 * anim, 2f, RenderUtil.applyOpacity(0xFF00AA00, anim));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY) && mouseButton == 0) {
            property.setValue(!property.getValue());
        }
    }
}
