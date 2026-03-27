package com.example.mod.gui.clickgui.component;

import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ColorPickerComponent extends UiComponent {
    private final String name;
    private final Supplier<Integer> getter;
    private final Consumer<Integer> setter;
    private boolean draggingSV;
    private boolean draggingHue;
    private float hue = 0f;
    private float sat = 1f;
    private float val = 1f;

    public ColorPickerComponent(String name, Supplier<Integer> getter, Consumer<Integer> setter) {
        super(0, 0, 0, 74);
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);

        if (!draggingSV && !draggingHue) {
            int color = getter.get() == null ? Color.HSBtoRGB(hue, sat, val) : getter.get();
            float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, null);
            hue = hsb[0];
            sat = hsb[1];
            val = hsb[2];
        } else if (Mouse.isButtonDown(0)) {
            updateColor(mouseX, mouseY, ry);
        } else {
            draggingSV = false;
            draggingHue = false;
        }

        float padding = 4f;
        float pickerX = x + padding;
        float pickerY = ry + padding + 10;
        float pickerW = width - (padding * 2);
        float pickerH = height - (padding * 2) - 10;
        float hueHeight = 6f;
        float svHeight = pickerH - hueHeight - 4f;

        RenderUtil.drawString(name, x + 4, ry + 3, MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha));
        int hueColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        RenderUtil.drawRect(pickerX, pickerY, pickerW, svHeight, hueColor);
        drawGradientRect(pickerX, pickerY, pickerW, svHeight, 0xFFFFFFFF, 0x00FFFFFF, true);
        drawGradientRect(pickerX, pickerY, pickerW, svHeight, 0x00000000, 0xFF000000, false);

        float indicatorX = pickerX + (sat * pickerW);
        float indicatorY = pickerY + ((1 - val) * svHeight);
        RenderUtil.drawOutlineRect(indicatorX - 3, indicatorY - 3, 6, 6, 1f, 0xFF000000);
        RenderUtil.drawOutlineRect(indicatorX - 2, indicatorY - 2, 4, 4, 1f, 0xFFFFFFFF);

        float hueY = pickerY + svHeight + 4f;
        drawRainbowRect(pickerX, hueY, pickerW, hueHeight);
        float hueIndicatorX = pickerX + (hue * pickerW);
        RenderUtil.drawRect(hueIndicatorX - 1, hueY, 2, hueHeight, 0xFFFFFFFF);
        RenderUtil.drawOutlineRect(pickerX - 1, pickerY - 1, pickerW + 2, pickerH + 2, 1f, MaterialTheme.getRGBWithAlpha(MaterialTheme.OUTLINE_COLOR, alpha));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        if (mouseButton != 0) return;
        float ry = y - scrollOffset;
        float padding = 4f;
        float svX = x + padding;
        float svY = ry + padding + 10;
        float svW = width - (padding * 2);
        float svH = (height - (padding * 2) - 10) - 10f;
        if (mouseX >= svX && mouseX <= svX + svW && mouseY >= svY && mouseY <= svY + svH) {
            draggingSV = true;
            updateColor(mouseX, mouseY, ry);
            return;
        }
        float hueY = svY + svH + 4f;
        if (mouseX >= svX && mouseX <= svX + svW && mouseY >= hueY && mouseY <= hueY + 6f) {
            draggingHue = true;
            updateColor(mouseX, mouseY, ry);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state, int scrollOffset) {
        if (state == 0) {
            draggingSV = false;
            draggingHue = false;
        }
    }

    private void updateColor(int mouseX, int mouseY, float ry) {
        float padding = 4f;
        float svX = x + padding;
        float svY = ry + padding + 10;
        float svW = width - (padding * 2);
        float svH = (height - (padding * 2) - 10) - 10f;
        if (draggingSV) {
            sat = Math.max(0f, Math.min(1f, (mouseX - svX) / svW));
            val = 1f - Math.max(0f, Math.min(1f, (mouseY - svY) / svH));
        }
        if (draggingHue) {
            hue = Math.max(0f, Math.min(1f, (mouseX - svX) / svW));
        }
        setter.accept(Color.HSBtoRGB(hue, sat, val));
    }

    private void drawRainbowRect(float x, float y, float width, float height) {
        int[] colors = {
                0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF, 0xFFFF0000
        };
        float segmentWidth = width / 6.0f;
        for (int i = 0; i < 6; i++) {
            drawGradientRect(x + (i * segmentWidth), y, segmentWidth, height, colors[i], colors[i + 1], true);
        }
    }

    private void drawGradientRect(float x, float y, float width, float height, int startColor, int endColor, boolean horizontal) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);

        if (horizontal) {
            worldrenderer.pos(x + width, y, 0).color(f5, f6, f7, f4).endVertex();
            worldrenderer.pos(x, y, 0).color(f1, f2, f3, f).endVertex();
            worldrenderer.pos(x, y + height, 0).color(f1, f2, f3, f).endVertex();
            worldrenderer.pos(x + width, y + height, 0).color(f5, f6, f7, f4).endVertex();
        } else {
            worldrenderer.pos(x + width, y, 0).color(f1, f2, f3, f).endVertex();
            worldrenderer.pos(x, y, 0).color(f1, f2, f3, f).endVertex();
            worldrenderer.pos(x, y + height, 0).color(f5, f6, f7, f4).endVertex();
            worldrenderer.pos(x + width, y + height, 0).color(f5, f6, f7, f4).endVertex();
        }

        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
