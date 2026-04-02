package com.example.mod.module.modules.render;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.util.TeamUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerESP extends BaseModule {
    private static volatile PlayerESP instance;

    private int red = 255;
    private int green = 0;
    private int blue = 0;
    private int alpha = 255;
    private boolean showTeammates = false;
    private boolean filledBox = true;
    private int filledAlpha = 40;
    private float lineWidth = 2.0F;

    public PlayerESP() {
        super("playeresp", "Draws boxes around players through walls.", Category.RENDER, false);
        instance = this;
    }

    public static PlayerESP getInstance() {
        return instance;
    }

    public static boolean shouldRender(EntityPlayer player) {
        PlayerESP module = instance;
        if (module == null || player == null || !module.enabled()) return false;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return false;
        if (player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) return false;
        if (player.deathTime > 0) return false;
        if (player.isInvisible()) return false;

        if (!module.showTeammates && TeamUtil.ignoreTeam(player.getName())) return false;

        return true;
    }

    public int getRed() { return red; }
    public void setRed(int red) { this.red = clamp(red); }

    public int getGreen() { return green; }
    public void setGreen(int green) { this.green = clamp(green); }

    public int getBlue() { return blue; }
    public void setBlue(int blue) { this.blue = clamp(blue); }

    public int getAlpha() { return alpha; }
    public void setAlpha(int alpha) { this.alpha = clamp(alpha); }

    public boolean isShowTeammates() { return showTeammates; }
    public void setShowTeammates(boolean showTeammates) { this.showTeammates = showTeammates; }

    public boolean isFilledBox() { return filledBox; }
    public void setFilledBox(boolean filledBox) { this.filledBox = filledBox; }

    public int getFilledAlpha() { return filledAlpha; }
    public void setFilledAlpha(int filledAlpha) { this.filledAlpha = Math.max(0, Math.min(120, filledAlpha)); }

    public float getLineWidth() { return lineWidth; }
    public void setLineWidth(float lineWidth) { this.lineWidth = Math.max(0.5F, Math.min(5.0F, lineWidth)); }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
