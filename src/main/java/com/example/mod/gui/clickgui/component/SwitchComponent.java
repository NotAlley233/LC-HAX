package com.example.mod.gui.clickgui.component;

import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.property.properties.BooleanProperty;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;

public class SwitchComponent extends UiComponent {
    private final BooleanProperty property;
    private float anim = 0f;

    public SwitchComponent(BooleanProperty property) {
        super(0, 0, 0, 22);
        this.property = property;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);
        anim = AnimationUtil.lerp(anim, property.getValue() ? 1f : 0f, 0.18f);

        RenderUtil.drawString(property.getName(), x + 4, ry + 6,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));

        float trackW = 26f;
        float trackH = 13f;
        float trackX = x + width - trackW - 4;
        float trackY = ry + (height - trackH) / 2f;

        int trackColor = MaterialTheme.blendColor(
                MaterialTheme.getRGBWithAlpha(MaterialTheme.SWITCH_OFF, alpha),
                MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha),
                anim);
        RoundedUtils.drawRoundedRect(trackX, trackY, trackW, trackH, trackH / 2f, trackColor);

        float knobSize = trackH - 3f;
        float knobTravel = trackW - knobSize - 3f;
        float knobX = trackX + 1.5f + knobTravel * anim;
        float knobY = trackY + 1.5f;
        RoundedUtils.drawRoundedRect(knobX, knobY, knobSize, knobSize, knobSize / 2f,
                new java.awt.Color(255, 255, 255, alpha).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        if (mouseButton == 0 && hovered(mouseX, mouseY, scrollOffset)) {
            property.setValue(!property.getValue());
        }
    }
}
