package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AutoBlockCheck {
    private int autoBlockTicks = 0;
    private final List<SwingSample> swingHistory = new ArrayList<>();
    private boolean lastSwingInProgress = false;
    private boolean lastBlockingState = false;
    private long lastSwingDetectedAt = 0L;
    private long blockingStartTime = 0L;

    public void anticheatCheck(EntityPlayer player, AntiCheat antiCheat) {
        if (!antiCheat.isDetectAutoBlock()) {
            clearState();
            return;
        }

        ItemStack held = player.getHeldItem();
        boolean isHoldingSword = held != null && held.getItem() instanceof ItemSword;
        boolean isBlocking = player.isBlocking();
        boolean isSwinging = player.isSwingInProgress;
        long now = System.currentTimeMillis();

        if (!isHoldingSword) {
            clearState();
            return;
        }

        if (isBlocking && !lastBlockingState) {
            blockingStartTime = now;
        } else if (!isBlocking) {
            blockingStartTime = 0L;
        }
        lastBlockingState = isBlocking;

        if (isSwinging && !lastSwingInProgress && now - lastSwingDetectedAt > 100L) {
            boolean wasBlockingBefore = isBlocking && blockingStartTime > 0L && now - blockingStartTime >= 150L;
            swingHistory.add(new SwingSample(now, wasBlockingBefore));
            lastSwingDetectedAt = now;
        }
        lastSwingInProgress = isSwinging;

        for (SwingSample sample : swingHistory) {
            if (sample.wasBlockingAfter == null) {
                long elapsed = now - sample.time;
                if (elapsed >= 150L && elapsed <= 200L) {
                    sample.wasBlockingAfter = isBlocking;
                } else if (elapsed > 200L) {
                    sample.wasBlockingAfter = false;
                }
            }
        }

        int validAutoBlockCount = 0;
        Iterator<SwingSample> iterator = swingHistory.iterator();
        while (iterator.hasNext()) {
            SwingSample sample = iterator.next();
            if (now - sample.time > 1000L) {
                iterator.remove();
                continue;
            }
            if (sample.wasBlockingAfter != null && sample.wasBlockingBefore && sample.wasBlockingAfter) {
                validAutoBlockCount++;
            }
        }

        if (validAutoBlockCount >= 2) {
            autoBlockTicks++;
            if (antiCheat.isDebugMessages() && autoBlockTicks > 5) {
                ChatUtil.sendFormatted("§e[AntiCheat] §f" + player.getName() + " AutoBlock ticks: " + autoBlockTicks);
            }
        } else if (autoBlockTicks > 0) {
            autoBlockTicks--;
        }
    }

    public boolean failedAutoBlock() {
        return autoBlockTicks > 10;
    }

    public void reset() {
        clearState();
    }

    private void clearState() {
        autoBlockTicks = 0;
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
