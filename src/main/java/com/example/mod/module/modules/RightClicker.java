package com.example.mod.module.modules;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import myau.util.MSTimer;
import myau.util.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class RightClicker extends BaseModule implements Tickable {
    private int minCPS = 12;
    private int maxCPS = 16;
    private int startDelay = 0;
    
    private final MSTimer clickTimer = new MSTimer();
    private final MSTimer delayTimer = new MSTimer();
    private long currentClickDelay = 0;
    private boolean wasHoldingRight = false;
    private Robot robot;

    public RightClicker() {
        super("rightclicker", "Automatically right clicks when holding blocks.", Category.COMBAT, false);
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

    public int getStartDelay() { return startDelay; }
    public void setStartDelay(int startDelay) { this.startDelay = startDelay; }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        
        // Ensure we are in game and no GUI is open
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) {
            wasHoldingRight = false;
            return;
        }

        // Check if player is holding a block
        ItemStack heldItem = mc.thePlayer.getHeldItem();
        if (heldItem == null) {
            wasHoldingRight = false;
            return;
        }

        // Only auto right click when holding blocks
        if (!(heldItem.getItem() instanceof ItemBlock)) {
            wasHoldingRight = false;
            return;
        }

        // Check if right mouse button is held down
        // We use JNI User32 to get the actual physical mouse state, bypassing LWJGL's Mouse queue
        // Because our Robot clicks will alter LWJGL's Mouse.isButtonDown state and cause an infinite loop.
        int rightClickButton = mc.gameSettings.keyBindUseItem.getKeyCode();
        boolean isHoldingRight = false;
        
        if (rightClickButton < 0) {
            // Check Windows API if available (Meowtils User32Util), fallback to LWJGL Mouse
            try {
                Class<?> user32Util = Class.forName("wtf.tatp.meowtils.util.User32Util");
                java.lang.reflect.Method holdingRight = user32Util.getMethod("holdingRightClick");
                isHoldingRight = (Boolean) holdingRight.invoke(null);
            } catch (Exception e) {
                // Fallback to LWJGL (this might cause the stickiness issue if Robot alters it)
                isHoldingRight = Mouse.isButtonDown(rightClickButton + 100);
            }
        }

        if (isHoldingRight) {
            if (!wasHoldingRight) {
                // Just started holding right click
                delayTimer.reset();
                wasHoldingRight = true;
            }

            // Check if initial start delay has passed
            if (delayTimer.hasTimePassed(startDelay)) {
                
                if (clickTimer.hasTimePassed(currentClickDelay)) {
                    doClick(mc);
                    
                    // Calculate next delay based on CPS bounds
                    int min = Math.min(minCPS, maxCPS);
                    int max = Math.max(minCPS, maxCPS);
                    
                    if (min <= 0) min = 1;
                    if (max <= 0) max = 1;
                    
                    // Generate a random delay in ms between (1000/max) and (1000/min)
                    long minDelay = 1000L / max;
                    long maxDelay = 1000L / min;
                    
                    if (minDelay == maxDelay) {
                        currentClickDelay = minDelay;
                    } else {
                        currentClickDelay = RandomUtil.nextLong(minDelay, maxDelay);
                    }
                    
                    clickTimer.reset();
                }
            }
        } else {
            // Not holding right click
            wasHoldingRight = false;
            clickTimer.reset();
            currentClickDelay = 0;
        }
    }

    private void doClick(Minecraft mc) {
        if (robot != null) {
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        } else {
            int keyCode = mc.gameSettings.keyBindUseItem.getKeyCode();
            net.minecraft.client.settings.KeyBinding.onTick(keyCode);
        }
    }
}
