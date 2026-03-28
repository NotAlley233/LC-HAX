package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class NoSlowCheck {
    private double lastPosX;
    private double lastPosZ;
    private int noSlowTicks = 0;
    private boolean initialized = false;

    public void anticheatCheck(EntityPlayer player, AntiCheat antiCheat) {
        if (!antiCheat.isDetectNoSlow()) {
            noSlowTicks = 0;
            initialized = false;
            return;
        }

        if (!initialized) {
            initialized = true;
            lastPosX = player.posX;
            lastPosZ = player.posZ;
            return;
        }

        double deltaX = player.posX - lastPosX;
        double deltaZ = player.posZ - lastPosZ;
        lastPosX = player.posX;
        lastPosZ = player.posZ;
        double speed = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        if (player.isSprinting() && player.isUsingItem() && !player.isInWater()) {
            double baseThreshold = 0.05D;
            PotionEffect speedEffect = player.getActivePotionEffect(Potion.moveSpeed);
            if (speedEffect != null) {
                int amplifier = speedEffect.getAmplifier();
                baseThreshold *= 1.0D + 0.2D * (amplifier + 1);
            }
            if (speed > baseThreshold) {
                noSlowTicks++;
                if (antiCheat.isDebugMessages() && noSlowTicks > 5) {
                    ChatUtil.sendFormatted("§e[AntiCheat] §f" + player.getName()
                            + " NoSlow ticks: " + noSlowTicks
                            + " | Speed: " + speed
                            + " | Threshold: " + baseThreshold);
                }
            } else {
                noSlowTicks = 0;
            }
        } else {
            noSlowTicks = 0;
        }
    }

    public boolean failedNoSlow() {
        return noSlowTicks > 20;
    }

    public void reset() {
        noSlowTicks = 0;
        initialized = false;
    }
}
