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
        super(0, 0, 0, 20);
        this.property = property;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);
        anim = AnimationUtil.lerp(anim, property.getValue() ? 1f : 0f, 0.2f);
        RenderUtil.drawString(property.getName(), x + 2, ry + 6, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));

        float trackX = x + width - 34;
        float trackY = ry + 5;
        int disabledColor = new java.awt.Color(60, 60, 65).getRGB();
        int enabledColor = MaterialTheme.getRGB(MaterialTheme.PRIMARY_COLOR);
        int switchColor = blendColor(disabledColor, enabledColor, anim);
        if (alpha < 255) {
            java.awt.Color c = new java.awt.Color(switchColor);
            switchColor = new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB();
        }
        RoundedUtils.drawRoundedRect(trackX, trackY, 22, 12, 6f, switchColor);
        float knobX = trackX + 1 + 14f * anim;
        RoundedUtils.drawRoundedRect(knobX, trackY + 1, 10, 10, 5f, 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        if (mouseButton == 0 && hovered(mouseX, mouseY, scrollOffset)) {
            property.setValue(!property.getValue());
        }
    }

    private int blendColor(int from, int to, float t) {
        float clamped = Math.max(0f, Math.min(1f, t));
        int fr = (from >> 16) & 255;
        int fg = (from >> 8) & 255;
        int fb = from & 255;
        int tr = (to >> 16) & 255;
        int tg = (to >> 8) & 255;
        int tb = to & 255;
        int r = (int) (fr + (tr - fr) * clamped);
        int g = (int) (fg + (tg - fg) * clamped);
        int b = (int) (fb + (tb - fb) * clamped);
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}
