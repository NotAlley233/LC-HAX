package com.example.mod.gui.clickgui.component;

public abstract class UiComponent {
    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected UiComponent(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(int mouseX, int mouseY, float partialTicks, int scrollOffset, float alphaProgress);

    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int scrollOffset) {
    }

    public void mouseReleased(int mouseX, int mouseY, int state, int scrollOffset) {
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public boolean isVisible() {
        return true;
    }

    public boolean hovered(int mouseX, int mouseY, int scrollOffset) {
        float ry = y - scrollOffset;
        return mouseX >= x && mouseX <= x + width && mouseY >= ry && mouseY <= ry + height;
    }

    public float getHeight() {
        return height;
    }

    public void setBounds(float x, float y, float width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }
}
