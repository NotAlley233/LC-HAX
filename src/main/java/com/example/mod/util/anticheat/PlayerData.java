package com.example.mod.util.anticheat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class PlayerData {
    public double speed;
    public int aboveVoidTicks;
    public int fastTick;
    public int autoBlockTicks;
    public int ticksExisted;
    public int lastSneakTick;
    public double posZ;
    public int sneakTicks;
    public int noSlowTicks;
    public double posY;
    public boolean sneaking;
    public double posX;
    private int resetTick;

    public void update(EntityPlayer entityPlayer) {
        int currentTick = entityPlayer.ticksExisted;
        this.posX = entityPlayer.posX - entityPlayer.prevPosX;
        this.posY = entityPlayer.posY - entityPlayer.prevPosY;
        this.posZ = entityPlayer.posZ - entityPlayer.prevPosZ;
        this.speed = Math.max(Math.abs(this.posX), Math.abs(this.posZ));

        if (currentTick - this.resetTick >= 20) {
            this.fastTick = 0;
            this.resetTick = currentTick;
        }

        if (this.speed >= 0.3D) {
            this.fastTick++;
            this.ticksExisted = currentTick;
        } else {
            this.fastTick = 0;
        }

        if (Math.abs(this.posY) >= 0.1D) {
            this.aboveVoidTicks = currentTick;
        }

        if (entityPlayer.isSneaking()) {
            this.lastSneakTick = currentTick;
        }

        if (entityPlayer.isBlocking() && entityPlayer.isUsingItem()) {
            this.autoBlockTicks++;
        } else {
            this.autoBlockTicks = 0;
        }

        if (entityPlayer.isSprinting() && entityPlayer.isUsingItem()) {
            this.noSlowTicks++;
        } else {
            this.noSlowTicks = 0;
        }

        ItemStack heldItem = entityPlayer.getHeldItem();
        if (entityPlayer.rotationPitch >= 70.0F && heldItem != null && heldItem.getItem() instanceof ItemBlock) {
            if (entityPlayer.swingProgressInt == 1) {
                if (!this.sneaking && entityPlayer.isSneaking()) {
                    this.sneakTicks++;
                } else {
                    this.sneakTicks = 0;
                }
            }
        } else {
            this.sneakTicks = 0;
        }
        this.sneaking = entityPlayer.isSneaking();
    }
}
