package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LegitScaffoldCheck {
    private static final Map<UUID, Long> lastCrouchStart = new HashMap<>();
    private static final Map<UUID, Long> lastCrouchEnd = new HashMap<>();
    private static final Map<UUID, Boolean> wasSneaking = new HashMap<>();
    private static final Map<UUID, Long> lastSwingTick = new HashMap<>();
    private static final Map<UUID, List<Integer>> crouchDurations = new HashMap<>();
    private static final Map<UUID, Long> lastFlagTick = new HashMap<>();
    private boolean flagged = false;

    public void anticheatCheck(EntityPlayer player, AntiCheat antiCheat) {
        if (player == null) {
            flagged = false;
            return;
        }
        UUID uuid = player.getUniqueID();
        if (!antiCheat.isDetectLegitScaffold()) {
            clearPlayerState(uuid);
            flagged = false;
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || player == mc.thePlayer) {
            flagged = false;
            return;
        }
        long tick = player.ticksExisted;
        boolean currSneak = player.isSneaking();
        boolean prevSneak = wasSneaking.getOrDefault(uuid, false);

        if (currSneak && !prevSneak) {
            lastCrouchStart.put(uuid, tick);
        } else if (!currSneak && prevSneak) {
            lastCrouchEnd.put(uuid, tick);
            long start = lastCrouchStart.getOrDefault(uuid, tick - 1L);
            int duration = (int) (tick - start);
            List<Integer> durations = crouchDurations.computeIfAbsent(uuid, k -> new ArrayList<>());
            durations.add(0, duration);
            if (durations.size() > 5) {
                durations.remove(5);
            }
        }
        wasSneaking.put(uuid, currSneak);

        ItemStack held = player.getHeldItem();
        boolean isHoldingBlock = held != null && held.getItem() instanceof ItemBlock;
        boolean swingTransition = player.swingProgress != player.prevSwingProgress;
        boolean isSwingingBlock = isHoldingBlock && (player.isSwingInProgress || swingTransition);
        if (isSwingingBlock) {
            lastSwingTick.put(uuid, tick);
        }

        if (player.rotationPitch >= 60.0F && isHoldingBlock && player.onGround) {
            long end = lastCrouchEnd.getOrDefault(uuid, 0L);
            long swing = lastSwingTick.getOrDefault(uuid, Long.MIN_VALUE);
            int crouchDuration = (int) (end - lastCrouchStart.getOrDefault(uuid, end - 1L));
            boolean quickCrouch = crouchDuration >= 1 && crouchDuration <= 2;
            boolean swingTiming = swing >= end && swing <= end + 1L;
            List<Integer> durations = crouchDurations.getOrDefault(uuid, Collections.emptyList());
            boolean consistent = durations.size() >= 3
                    && durations.get(0) <= 2
                    && durations.get(1) <= 2
                    && durations.get(2) <= 2;

            if (quickCrouch && swingTiming && consistent) {
                long lastFlag = lastFlagTick.getOrDefault(uuid, 0L);
                if (tick - lastFlag >= 60L) {
                    flagged = true;
                    lastFlagTick.put(uuid, tick);
                } else {
                    flagged = false;
                }
            } else {
                flagged = false;
            }
        } else {
            flagged = false;
        }
    }

    public boolean failedLegitScaffold() {
        return flagged;
    }

    public void reset() {
        flagged = false;
    }

    private static void clearPlayerState(UUID uuid) {
        lastCrouchStart.remove(uuid);
        lastCrouchEnd.remove(uuid);
        wasSneaking.remove(uuid);
        lastSwingTick.remove(uuid);
        crouchDurations.remove(uuid);
        lastFlagTick.remove(uuid);
    }

    public static void clear() {
        lastCrouchStart.clear();
        lastCrouchEnd.clear();
        wasSneaking.clear();
        lastSwingTick.clear();
        crouchDurations.clear();
        lastFlagTick.clear();
    }
}
