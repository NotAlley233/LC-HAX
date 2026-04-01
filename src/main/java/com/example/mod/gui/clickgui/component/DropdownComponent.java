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
        super(0, 0, 0, 22);
        this.property = property;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);
        float target = expanded ? property.getModes().size() * ITEM_HEIGHT : 0f;
        animHeight = AnimationUtil.lerp(animHeight, target, 0.2f);
        if (Math.abs(animHeight - target) < 0.5f) animHeight = target;
        this.height = 22 + animHeight;

        RoundedUtils.drawRoundedRect(x + 2, ry, width - 4, 22, MaterialTheme.CORNER_RADIUS_ITEM,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.DROPDOWN_BG, alpha));
        RenderUtil.drawString(property.getName(), x + 8, ry + 6,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));
        String current = property.getValue();
        RenderUtil.drawString(current, x + width - 8 - RenderUtil.getStringWidth(current), ry + 6,
                MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha));

        if (animHeight > 1f) {
            float listY = ry + 24;
            RoundedUtils.drawRoundedRect(x + 2, listY, width - 4, animHeight,
                    MaterialTheme.CORNER_RADIUS_ITEM,
                    MaterialTheme.getRGBWithAlpha(MaterialTheme.DROPDOWN_BG, alpha));
            RenderUtil.scissor(x + 2, listY, width - 4, animHeight);

            float cy = listY;
            for (String mode : property.getModes()) {
                if (cy + ITEM_HEIGHT > listY + animHeight) break;
                boolean itemHovered = mouseX >= x + 2 && mouseX <= x + width - 2
                        && mouseY >= cy && mouseY <= cy + ITEM_HEIGHT;
                boolean isSelected = mode.equalsIgnoreCase(current);

                if (itemHovered) {
                    RoundedUtils.drawRoundedRect(x + 4, cy + 1, width - 8, ITEM_HEIGHT - 2,
                            3f, MaterialTheme.getRGBWithAlpha(MaterialTheme.DROPDOWN_HOVER, alpha));
                }

                int textColor = isSelected
                        ? MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha)
                        : MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha);

                if (isSelected) {
                    RoundedUtils.drawCircle(x + 12, cy + ITEM_HEIGHT / 2f, 2f,
                            MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha));
                    RenderUtil.drawString(mode, x + 18, cy + 5, textColor);
                } else {
                    RenderUtil.drawString(mode, x + 10, cy + 5, textColor);
                }

                cy += ITEM_HEIGHT;
            }
            RenderUtil.releaseScissor();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        float ry = y - scrollOffset;
        if (mouseX >= x && mouseX <= x + width && mouseY >= ry && mouseY <= ry + 22) {
            if (mouseButton == 0 || mouseButton == 1) {
                expanded = !expanded;
            }
            return;
        }
        if (expanded && mouseButton == 0) {
            float listY = ry + 24;
            float cy = listY;
            for (String mode : property.getModes()) {
                if (mouseX >= x + 2 && mouseX <= x + width - 2 && mouseY >= cy && mouseY <= cy + ITEM_HEIGHT) {
                    property.setValue(mode);
                    expanded = false;
                    return;
                }
                cy += ITEM_HEIGHT;
            }
        }
    }
}
