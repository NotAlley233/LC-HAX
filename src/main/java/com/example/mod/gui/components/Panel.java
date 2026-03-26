package com.example.mod.gui.components;

import com.example.mod.module.Category;
import com.example.mod.module.Module;
import com.example.mod.util.render.AnimationUtil;
import com.example.mod.util.render.RenderUtil;
import com.example.mod.util.render.RoundedUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class Panel extends Component {
    private final String title;
    private final List<ModuleButton> buttons = new ArrayList<>();
    
    private boolean dragging;
    private float dragX, dragY;
    private boolean expanded = true;
    
    // Animation targets
    private float currentHeight;
    private final float headerHeight = 18;

    public Panel(Category category, float x, float y, float width) {
        this(category.getName(), x, y, width);
    }

    public Panel(String title, float x, float y, float width) {
        super(x, y, width, 18);
        this.title = title;
        this.currentHeight = headerHeight;
    }

    public void addButton(ModuleButton button) {
        this.buttons.add(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            this.x = mouseX - dragX;
            this.y = mouseY - dragY;
        }

        // Calculate target height
        float targetHeight = headerHeight;
        if (expanded) {
            float buttonsHeight = 0;
            for (ModuleButton button : buttons) {
                buttonsHeight += button.getHeight();
            }
            targetHeight += buttonsHeight;
        }

        // Smooth height transition
        currentHeight = AnimationUtil.lerp(currentHeight, targetHeight, 0.2f);
        this.height = currentHeight;

        // Draw header and background with rounded corners
        int bgColor = new Color(25, 25, 25, 220).getRGB();
        int headerColor = new Color(35, 35, 35, 255).getRGB();

        // Full background
        RoundedUtils.drawRoundedRect(x, y, width, currentHeight, 6f, bgColor);
        // Header background
        RoundedUtils.drawRoundedRect(x, y, width, headerHeight, 6f, headerColor);

        RenderUtil.drawString(title, x + 6, y + 5, 0xFFFFFFFF);

        // Draw background for buttons if expanded
        if (currentHeight > headerHeight) {
            // Enable Scissor to hide components outside the animation bounds
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtil.prepareScissorBox(x, y + headerHeight, x + width, y + currentHeight);

            float currentY = y + headerHeight;
            for (ModuleButton button : buttons) {
                button.setX(this.x);
                button.setY(currentY);
                button.setWidth(this.width);
                button.drawScreen(mouseX, mouseY, partialTicks);
                currentY += button.getHeight();
            }
            
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHoveredHeader(mouseX, mouseY)) {
            if (mouseButton == 0) { // Left click = drag
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            } else if (mouseButton == 1) { // Right click = expand/collapse
                expanded = !expanded;
            }
        }

        if (expanded && mouseY > y + headerHeight && mouseY < y + currentHeight) {
            for (ModuleButton button : buttons) {
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            dragging = false;
        }
        
        if (expanded) {
            for (ModuleButton button : buttons) {
                button.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (expanded) {
            for (ModuleButton button : buttons) {
                button.keyTyped(typedChar, keyCode);
            }
        }
    }

    private boolean isHoveredHeader(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + headerHeight;
    }
}
