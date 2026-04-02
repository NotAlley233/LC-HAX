package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.anticheat.AntiCheatCheckIds;
import com.example.mod.util.anticheat.StarfishStylePlayerState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Starfish EagleA: diagonal eagle / legit scaffold patterns.
 */
public class EagleACheck {

    private static final double[] CARDINAL_DEG = {0, 90, 180, 270};

    public void check(EntityPlayer player, AntiCheat antiCheat, StarfishStylePlayerState state) {
        if (!antiCheat.isDetectEagle()) {
            return;
        }

        boolean isLookingDown = player.rotationPitch >= 30.0F;
        boolean isOnGround = player.onGround;
        ItemStack held = player.getHeldItem();
        boolean isHoldingBlock = held != null && held.getItem() instanceof ItemBlock;
        boolean isSwingingBlock = player.isSwingInProgress && isHoldingBlock;

        double horizontalSpeed = state.horizontalSpeedBps;
        boolean isMovingFast = horizontalSpeed > 2.0D;

        double dx = player.posX - player.prevPosX;
        double dz = player.posZ - player.prevPosZ;
        double movementAngle = Math.atan2(dz, dx) * 180.0 / Math.PI;
        if (movementAngle < 0) {
            movementAngle += 360.0;
        }
        boolean isMovingStraight = false;
        for (double a : CARDINAL_DEG) {
            if (Math.abs(movementAngle - a) <= 15.0 || Math.abs(movementAngle - a - 360.0) <= 15.0) {
                isMovingStraight = true;
                break;
            }
        }
        boolean isMovingDiagonal = !isMovingStraight && horizontalSpeed > 0.1D;

        int shiftCount = state.shiftStartsInWindow();
        boolean hasExcessiveShifts = shiftCount > 6 && horizontalSpeed > 2.5D;

        boolean isEagle = isLookingDown && isOnGround && isSwingingBlock
                && isMovingDiagonal && isMovingFast && hasExcessiveShifts;

        if (isEagle) {
            state.addViolation(AntiCheatCheckIds.EAGLE_A, 3);
        } else {
            state.reduceViolation(AntiCheatCheckIds.EAGLE_A, 3);
        }
    }
}
