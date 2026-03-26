package com.example.mod.module.modules;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Tickable;
import com.example.mod.mixins.MinecraftAccessor;
import myau.util.MSTimer;
import myau.util.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.lang.reflect.Method;

public class AutoClicker extends BaseModule implements Tickable {
    private int minCPS = 8;
    private int maxCPS = 12;
    private boolean blocks = false;
    private boolean breakBlocks = false;
    
    private final MSTimer timer = new MSTimer();
    private long currentDelay = 0;
    private Method clickMouseMethod = null;

    public AutoClicker() {
        super(false);
        try {
            // Fallback reflection in case mixin fails due to mapping issues
            clickMouseMethod = Minecraft.class.getDeclaredMethod("clickMouse");
            clickMouseMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            try {
                // MCP mapped name fallback
                clickMouseMethod = Minecraft.class.getDeclaredMethod("func_147116_af");
                clickMouseMethod.setAccessible(true);
            } catch (NoSuchMethodException ignored) {
            }
        }
    }

    @Override
    public String name() {
        return "autoclicker";
    }

    @Override
    public String description() {
        return "Automatically clicks for you.";
    }

    public int getMinCPS() { return minCPS; }
    public void setMinCPS(int minCPS) { this.minCPS = minCPS; }

    public int getMaxCPS() { return maxCPS; }
    public void setMaxCPS(int maxCPS) { this.maxCPS = maxCPS; }

    public boolean isBlocks() { return blocks; }
    public void setBlocks(boolean blocks) { this.blocks = blocks; }

    public boolean isBreakBlocks() { return breakBlocks; }
    public void setBreakBlocks(boolean breakBlocks) { this.breakBlocks = breakBlocks; }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        
        // Ensure we are in game, no GUI is open, and the attack key is held
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) {
            return;
        }

        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            // Block check logic
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                if (blockPos != null) {
                    Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                    if (block.getMaterial() != Material.air) {
                        if (!this.blocks) {
                            return;
                        }
                        
                        if (this.breakBlocks && mc.playerController != null && mc.playerController.getIsHittingBlock()) {
                            return;
                        }
                    }
                }
            }

            if (timer.hasTimePassed(currentDelay)) {
                doClick(mc);
                
                // Calculate next delay based on CPS bounds
                int min = Math.min(minCPS, maxCPS);
                int max = Math.max(minCPS, maxCPS);
                
                // Avoid divide by zero
                if (min <= 0) min = 1;
                if (max <= 0) max = 1;
                
                int targetCPS = RandomUtil.nextInt(min, max);
                currentDelay = 1000L / targetCPS;
                
                timer.reset();
            }
        } else {
            // Reset timer when not clicking so the first click is immediate
            timer.reset();
            currentDelay = 0;
        }
    }

    private void doClick(Minecraft mc) {
        try {
            // Primary method: Mixin Invoker
            ((MinecraftAccessor) mc).invokeClickMouse();
        } catch (Throwable t) {
            // Fallback: Reflection
            if (clickMouseMethod != null) {
                try {
                    clickMouseMethod.invoke(mc);
                } catch (Exception ignored) {
                }
            }
        }
    }
}