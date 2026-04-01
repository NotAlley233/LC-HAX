package com.example.mod.gui.clickgui.component;

import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.property.properties.IntProperty;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;

public class SliderComponent extends UiComponent {
    private final IntProperty property;
    private boolean dragging = false;
    private float display = 0f;

    public SliderComponent(IntProperty property) {
        super(0, 0, 0, 24);
        this.property = property;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);
        int min = property.getMin();
        int max = property.getMax();

        if (dragging) {
            float pct = Math.max(0f, Math.min(1f, (mouseX - (x + 4f)) / (width - 8f)));
            property.setValue((int) (min + (max - min) * pct));
        }

        float target = (max == min) ? 0 : (property.getValue() - min) / (float) (max - min);
        display = AnimationUtil.lerp(display, target, 0.18f);

        RenderUtil.drawString(property.getName(), x + 4, ry + 2,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));
        String valStr = String.valueOf(property.getValue());
        RenderUtil.drawString(valStr, x + width - RenderUtil.getStringWidth(valStr) - 4, ry + 2,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha));

        float trackH = 5f;
        float trackY = ry + height - 9;
        float trackW = width - 8f;
        RoundedUtils.drawRoundedRect(x + 4, trackY, trackW, trackH, trackH / 2f,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.SLIDER_TRACK, alpha));

        float filledW = trackW * display;
        if (filledW > 1f) {
            RoundedUtils.drawRoundedRect(x + 4, trackY, filledW, trackH, trackH / 2f,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha));
        }

        float knobCx = x + 4 + trackW * display;
        float knobCy = trackY + trackH / 2f;
        RoundedUtils.drawCircle(knobCx, knobCy, 4f,
                new java.awt.Color(255, 255, 255, alpha).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        if (mouseButton == 0 && hovered(mouseX, mouseY, scrollOffset)) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state, int scrollOffset) {
        if (state == 0) dragging = false;
    }
}
