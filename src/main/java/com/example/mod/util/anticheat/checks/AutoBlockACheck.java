package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.ChatUtil;
import com.example.mod.util.anticheat.AntiCheatCheckIds;
import com.example.mod.util.anticheat.StarfishStylePlayerState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Starfish AutoBlockA: swing history with blocking before and after swing.
 */
public class AutoBlockACheck {
    private final List<SwingSample> swingHistory = new ArrayList<>();
    private boolean lastSwingInProgress;
    private boolean lastBlockingState;
    private long lastSwingDetectedAt;
    private long blockingStartTime;

    public void check(EntityPlayer player, AntiCheat antiCheat, StarfishStylePlayerState state, long nowMs) {
        if (!antiCheat.isDetectAutoBlock()) {
            clearState();
            return;
        }

        ItemStack held = player.getHeldItem();
        boolean isHoldingSword = held != null && held.getItem() instanceof ItemSword;
        boolean isBlocking = player.isBlocking();
        boolean isSwinging = player.isSwingInProgress;

        if (!isHoldingSword) {
            clearState();
            state.reduceViolation(AntiCheatCheckIds.AUTO_BLOCK_A, 1);
            return;
        }

        if (isBlocking && !lastBlockingState) {
            blockingStartTime = nowMs;
        } else if (!isBlocking) {
            blockingStartTime = 0L;
        }
        lastBlockingState = isBlocking;

        if (isSwinging && !lastSwingInProgress && nowMs - lastSwingDetectedAt > 100L) {
            boolean wasBlockingBefore = isBlocking && blockingStartTime > 0L && nowMs - blockingStartTime >= 150L;
            swingHistory.add(new SwingSample(nowMs, wasBlockingBefore));
            lastSwingDetectedAt = nowMs;
            if (swingHistory.size() > 20) {
                swingHistory.remove(0);
            }
        }
        lastSwingInProgress = isSwinging;

        for (SwingSample swing : swingHistory) {
            if (swing.wasBlockingAfter == null) {
                long timeSinceSwing = nowMs - swing.time;
                if (timeSinceSwing >= 150L && timeSinceSwing <= 200L) {
                    swing.wasBlockingAfter = isBlocking;
                } else if (timeSinceSwing > 200L) {
                    swing.wasBlockingAfter = false;
                }
            }
        }

        int autoBlockCount = 0;
        Iterator<SwingSample> iterator = swingHistory.iterator();
        while (iterator.hasNext()) {
            SwingSample swing = iterator.next();
            if (nowMs - swing.time > 1000L) {
                iterator.remove();
                continue;
            }
            if (swing.wasBlockingAfter != null && swing.wasBlockingBefore && swing.wasBlockingAfter) {
                autoBlockCount++;
            }
        }

        if (autoBlockCount >= 2) {
            state.addViolation(AntiCheatCheckIds.AUTO_BLOCK_A, 1);
            if (antiCheat.isDebugMessages()) {
                ChatUtil.sendFormatted("§e[AntiCheat] §f" + player.getName() + " AutoBlockA count: " + autoBlockCount);
            }
        } else {
            state.reduceViolation(AntiCheatCheckIds.AUTO_BLOCK_A, 1);
        }
    }

    public void reset() {
        clearState();
    }

    private void clearState() {
        swingHistory.clear();
        lastSwingInProgress = false;
        lastBlockingState = false;
        lastSwingDetectedAt = 0L;
        blockingStartTime = 0L;
    }

    private static final class SwingSample {
        private final long time;
        private final boolean wasBlockingBefore;
        private Boolean wasBlockingAfter;

        private SwingSample(long time, boolean wasBlockingBefore) {
            this.time = time;
            this.wasBlockingBefore = wasBlockingBefore;
        }
    }
}
