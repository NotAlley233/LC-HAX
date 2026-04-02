package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.anticheat.AntiCheatCheckIds;
import com.example.mod.util.anticheat.StarfishStylePlayerState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Starfish ScaffoldA: fast flat scaffold (default off in Starfish).
 */
public class ScaffoldACheck {

    public void check(EntityPlayer player, AntiCheat antiCheat, StarfishStylePlayerState state) {
        if (!antiCheat.isDetectScaffold()) {
            return;
        }

        if (player.posY > 100.0D) {
            state.reduceViolation(AntiCheatCheckIds.SCAFFOLD_A, 1);
            return;
        }

        double horizontalSpeed = state.horizontalSpeedBps;
        boolean isLookingDown = player.rotationPitch >= 25.0F;
        ItemStack held = player.getHeldItem();
        boolean isHoldingBlock = held != null && held.getItem() instanceof ItemBlock;
        boolean isPlacingBlocks = player.isSwingInProgress && isHoldingBlock;
        boolean isMovingFast = horizontalSpeed > 5.0D;
        boolean isNotSneaking = !player.isSneaking();
        boolean isFlat = Math.abs(state.verticalSpeedBps) < 0.1D;

        boolean isScaffold = isLookingDown && isPlacingBlocks && isMovingFast && isNotSneaking && isFlat;

        if (isScaffold) {
            state.addViolation(AntiCheatCheckIds.SCAFFOLD_A, 1);
        } else {
            state.reduceViolation(AntiCheatCheckIds.SCAFFOLD_A, 1);
        }
    }
}
