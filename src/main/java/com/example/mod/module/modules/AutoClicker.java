package com.example.mod.module.modules;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import myau.util.MSTimer;
import myau.util.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.lwjgl.input.Mouse;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class AutoClicker extends BaseModule implements Tickable {
    private int minCPS = 8;
    private int maxCPS = 12;
    private boolean breakBlocks = false;
    
    private final MSTimer timer = new MSTimer();
    private long currentDelay = 0;
    private Robot robot;

    public AutoClicker() {
        super("autoclicker", "Automatically clicks for you.", Category.COMBAT, false);
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMinCPS() { return minCPS; }
    public void setMinCPS(int minCPS) { this.minCPS = minCPS; }

    public int getMaxCPS() { return maxCPS; }
    public void setMaxCPS(int maxCPS) { this.maxCPS = maxCPS; }

    public boolean isBreakBlocks() { return breakBlocks; }
    public void setBreakBlocks(boolean breakBlocks) { this.breakBlocks = breakBlocks; }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        
        // Ensure we are in game, no GUI is open
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) {
            return;
        }

        // Use JNI User32 to get physical mouse state.
        // Minecraft's keyBindAttack.isKeyDown() or LWJGL Mouse.isButtonDown can get stuck or altered by our own Robot clicks.
        int attackKey = mc.gameSettings.keyBindAttack.getKeyCode();
        boolean isHoldingLeft = false;
        
        if (attackKey < 0) {
            try {
                Class<?> user32Util = Class.forName("wtf.tatp.meowtils.util.User32Util");
                java.lang.reflect.Method holdingLeft = user32Util.getMethod("holdingLeftClick");
                isHoldingLeft = (Boolean) holdingLeft.invoke(null);
            } catch (Exception e) {
                isHoldingLeft = Mouse.isButtonDown(attackKey + 100);
            }
        }

        if (isHoldingLeft) {
            // Block check logic
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                if (blockPos != null) {
                    Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                    if (block.getMaterial() != Material.air) {
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
                
                // Generate a random delay in ms between (1000/max) and (1000/min)
                // This ensures the actual delay falls directly within the requested CPS range
                long minDelay = 1000L / max;
                long maxDelay = 1000L / min;
                
                if (minDelay == maxDelay) {
                    currentDelay = minDelay;
                } else {
                    currentDelay = RandomUtil.nextLong(minDelay, maxDelay);
                }
                
                timer.reset();
            }
        } else {
            // Reset timer when not clicking so the first click is immediate
            timer.reset();
            currentDelay = 0;
        }
    }

    private void doClick(Minecraft mc) {
        if (robot != null) {
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        } else {
            int keyCode = mc.gameSettings.keyBindAttack.getKeyCode();
            net.minecraft.client.settings.KeyBinding.onTick(keyCode);
        }
    }
}