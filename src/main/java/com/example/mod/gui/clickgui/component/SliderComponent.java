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
        super(0, 0, 0, 20);
        this.property = property;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);
        int min = property.getMin();
        int max = property.getMax();
        if (dragging) {
            float pct = Math.max(0f, Math.min(1f, (mouseX - (x + 6f)) / (width - 12f)));
            property.setValue((int) (min + (max - min) * pct));
        }
        float target = (property.getValue() - min) / (float) (max - min);
        display = AnimationUtil.lerp(display, target, 0.2f);

        RenderUtil.drawString(property.getName(), x + 2, ry + 2, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));
        String valStr = String.valueOf(property.getValue());
        RenderUtil.drawString(valStr, x + width - RenderUtil.getStringWidth(valStr) - 2, ry + 2, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));
        int trackY = (int) (ry + height - 8);
        RoundedUtils.drawRoundedRect(x + 2, trackY, width - 4, 4, 2f, new java.awt.Color(40, 40, 45).getRGB());
        int accent = MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha);
        RoundedUtils.drawRoundedRect(x + 2, trackY, (width - 4) * display, 4, 2f, accent, true, false, false, true);
        float knobX = x + 2 + (width - 4) * display - 2;
        RoundedUtils.drawRoundedRect(knobX, trackY - 2, 4, 8, 2f, 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        if (mouseButton == 0 && hovered(mouseX, mouseY, scrollOffset)) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state, int scrollOffset) {
        if (state == 0) {
            dragging = false;
        }
    }
}
