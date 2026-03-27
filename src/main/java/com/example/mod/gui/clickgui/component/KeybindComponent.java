package com.example.mod.gui.clickgui.component;

import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KeybindComponent extends UiComponent {
    private final String name;
    private final Supplier<Integer> getter;
    private final Consumer<Integer> setter;
    private boolean binding;

    public KeybindComponent(String name, Supplier<Integer> getter, Consumer<Integer> setter) {
        super(0, 0, 0, 20);
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress) {
        float ry = y - scrollOffset;
        int alpha = (int) (255 * alphaProgress);
        String title = name == null ? "Keybind" : name;
        String value;
        if (binding) {
            value = "...";
        } else {
            int key = Math.max(0, getter.get());
            value = key == Keyboard.KEY_NONE ? "None" : Keyboard.getKeyName(key);
        }
        int titleColor = MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR, alpha);
        int valueColor = binding
                ? MaterialTheme.getRGBWithAlpha(MaterialTheme.PRIMARY_COLOR, alpha)
                : MaterialTheme.getRGBWithAlpha(MaterialTheme.TEXT_COLOR_SECONDARY, alpha);
        RenderUtil.drawString(title, x + 6, ry + 4, titleColor);
        RenderUtil.drawString(value, x + width - 6 - RenderUtil.getStringWidth(value), ry + 4, valueColor);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
        if (mouseButton == 0 && hovered(mouseX, mouseY, scrollOffset)) {
            binding = !binding;
            return;
        }
        if (binding && !hovered(mouseX, mouseY, scrollOffset)) {
            binding = false;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (!binding) return;
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_BACK) {
            setter.accept(0);
        } else {
            setter.accept(keyCode);
        }
        binding = false;
    }
}
