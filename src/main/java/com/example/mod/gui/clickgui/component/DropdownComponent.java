package com.example.mod.gui.clickgui.component;

import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.property.properties.ModeProperty;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;

public class DropdownComponent extends UiComponent {
    private final ModeProperty property;
    private boolean expanded;
    private float animHeight;
    private static final int ITEM_HEIGHT = 18;

    public DropdownComponent(ModeProperty property) {
        super(0, 0, 0, 20);
        this.property = property;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);
        float target = expanded ? property.getModes().size() * ITEM_HEIGHT : 0f;
        animHeight = AnimationUtil.lerp(animHeight, target, 0.22f);
        this.height = 20 + animHeight;

        RoundedUtils.drawRoundedRect(x + 2, ry, width - 4, 20, 4f, new java.awt.Color(30, 30, 35, alpha).getRGB());
        RenderUtil.drawString(property.getName(), x + 6, ry + 3, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));
        String current = property.getValue();
        RenderUtil.drawString(current, x + width - 6 - RenderUtil.getStringWidth(current), ry + 3, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha));

        if (animHeight > 1f) {
            float cy = ry + 22;
            RoundedUtils.drawRoundedRect(x + 2, cy, width - 4, animHeight, 4f, new java.awt.Color(20, 20, 24, 240).getRGB());
            RenderUtil.scissor(x, cy, width, animHeight);
            for (String mode : property.getModes()) {
                if (cy + ITEM_HEIGHT > ry + 22 + animHeight) break;
                int color = mode.equalsIgnoreCase(current)
                        ? MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha)
                        : MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha);
                RenderUtil.drawString(mode, x + 8, cy + 5, color);
                cy += ITEM_HEIGHT;
            }
            RenderUtil.releaseScissor();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        if (!hovered(mouseX, mouseY, scrollOffset)) return;
        if (mouseButton == 1) {
            expanded = !expanded;
            return;
        }
        if (mouseButton != 0) return;
        if (!expanded) {
            expanded = true;
            return;
        }
        float ry = y - scrollOffset + 22;
        for (String mode : property.getModes()) {
            if (mouseY >= ry && mouseY <= ry + ITEM_HEIGHT) {
                property.setValue(mode);
                expanded = false;
                return;
            }
            ry += ITEM_HEIGHT;
        }
    }
}
