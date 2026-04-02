package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.anticheat.AntiCheatCheckIds;
import com.example.mod.util.anticheat.StarfishStylePlayerState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Starfish TowerA: fast vertical tower while placing (default off in Starfish).
 */
public class TowerACheck {

    public void check(EntityPlayer player, AntiCheat antiCheat, StarfishStylePlayerState state, long nowMs) {
        if (!antiCheat.isDetectTower()) {
            return;
        }

        double verticalSpeed = state.verticalSpeedBps;
        double horizontalSpeed = state.horizontalSpeedBps;

        boolean isLookingDown = player.rotationPitch >= 30.0F;
        ItemStack held = player.getHeldItem();
        boolean isHoldingBlock = held != null && held.getItem() instanceof ItemBlock;
        boolean isSwingingBlock = player.isSwingInProgress && isHoldingBlock;
        boolean hasNoJumpBoost = !state.hasJumpBoost(player);
        boolean isAscendingFast = verticalSpeed > 5.5D;

        double verticalToHorizontalRatio = horizontalSpeed > 0 ? verticalSpeed / horizontalSpeed : verticalSpeed;
        boolean hasProperTowerRatio = verticalToHorizontalRatio >= 0.8D;

        boolean hasRecentDamage = state.getLastDamagedMs() > 0L && (nowMs - state.getLastDamagedMs()) < 500L;

        ArrayList<StarfishStylePlayerState.HeightSample> history = state.getTowerHeightHistory();

        if (nowMs - state.getTowerLastResetMs() > 2000L) {
            history.clear();
            state.setTowerLastResetMs(nowMs);
        }

        if (isLookingDown && isSwingingBlock && isAscendingFast && hasProperTowerRatio && hasNoJumpBoost && !hasRecentDamage) {
            history.add(new StarfishStylePlayerState.HeightSample(player.posY, nowMs));
            if (history.size() > 15) {
                history.remove(0);
            }
        }

        if (history.size() >= 8) {
            StarfishStylePlayerState.HeightSample start = history.get(0);
            StarfishStylePlayerState.HeightSample end = history.get(history.size() - 1);
            double totalHeightGain = end.y - start.y;
            double timeSpan = (end.timeMs - start.timeMs) / 1000.0D;

            int consistentRiseCount = 0;
            for (int i = 1; i < history.size(); i++) {
                if (history.get(i).y > history.get(i - 1).y) {
                    consistentRiseCount++;
                }
            }
            double consistencyRatio = (double) consistentRiseCount / (history.size() - 1);
            boolean hasConsistentRise = consistencyRatio >= 0.8D;
            boolean hasSignificantHeight = totalHeightGain >= 3.0D;
            boolean hasGoodTimespan = timeSpan >= 0.4D && timeSpan <= 1.5D;

            if (hasConsistentRise && hasSignificantHeight && hasGoodTimespan) {
                state.addViolation(AntiCheatCheckIds.TOWER_A, 2);
            } else {
                state.reduceViolation(AntiCheatCheckIds.TOWER_A, 2);
            }
        }
    }
}
