package com.example.mod.gui.clickgui;

import com.example.mod.gui.clickgui.theme.MaterialTheme;
import com.example.mod.module.Category;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.PropertyManager;
import com.example.mod.util.render.GuiAnimationUtil;
import com.example.mod.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {
    private final List<Frame> frames = new ArrayList<>();
    private int scrollY = 0;
    private int targetScrollY = 0;
    private double velocity = 0;
    private boolean closing = false;
    private long animationStart = 0L;
    private static final long ANIM_MS = 250L;
    private static final double FRICTION = 0.85;
    private static final double SNAP_STRENGTH = 0.15;

    public ClickGuiScreen(ModuleManager moduleManager, PropertyManager propertyManager) {
        float x = 20;
        float y = 20;
        float width = 110;
        float header = 24;
        for (Category category : Category.values()) {
            List<Module> modules = new ArrayList<>();
            for (Module module : moduleManager.all()) {
                if (module.category() == category) modules.add(module);
            }
            modules.sort(Comparator.comparing(Module::name, String.CASE_INSENSITIVE_ORDER));
            if (!modules.isEmpty()) {
                frames.add(new Frame(category, x, y, width, header, modules, propertyManager));
                x += width + 15;
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        animationStart = System.currentTimeMillis();
        closing = false;
        scrollY = 0;
        targetScrollY = 0;
        velocity = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateScroll();
        float progress = animationProgress();
        drawRect(0, 0, this.width, this.height, RenderUtil.applyOpacity(0xFF09090B, 0.45f * progress));
        for (Frame frame : frames) {
            frame.draw(mouseX, mouseY, partialTicks, scrollY, progress);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (int i = frames.size() - 1; i >= 0; i--) {
            Frame frame = frames.get(i);
            if (frame.mouseClicked(mouseX, mouseY, mouseButton, scrollY)) {
                frames.remove(i);
                frames.add(frame);
                break;
            }
        }
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Frame frame : frames) {
            frame.mouseReleased(mouseX, mouseY, state, scrollY);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (!closing) {
                closing = true;
                animationStart = System.currentTimeMillis();
            } else {
                mc.displayGuiScreen(null);
            }
            return;
        }
        for (Frame frame : frames) {
            frame.keyTyped(typedChar, keyCode);
        }
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            velocity += wheel > 0 ? -30 : 30;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void updateScroll() {
        targetScrollY += (int) velocity;
        velocity *= FRICTION;
        int max = maxScroll();
        targetScrollY = Math.max(0, Math.min(max, targetScrollY));
        scrollY += (int) ((targetScrollY - scrollY) * SNAP_STRENGTH);
        if (Math.abs(targetScrollY - scrollY) < 1 && Math.abs(velocity) < 0.5) {
            scrollY = targetScrollY;
            velocity = 0;
        }
    }

    private int maxScroll() {
        float bottom = 0;
        for (Frame frame : frames) {
            bottom = Math.max(bottom, frame.getBottom());
        }
        return Math.max(0, (int) bottom - this.height + 16);
    }

    private float animationProgress() {
        float t = GuiAnimationUtil.clamp01((System.currentTimeMillis() - animationStart) / (float) ANIM_MS);
        float eased = GuiAnimationUtil.easeOutCubic(t);
        if (closing) {
            float r = 1f - eased;
            if (r <= 0.02f) {
                mc.displayGuiScreen(null);
                return 0f;
            }
            return r;
        }
        return eased;
    }
}
