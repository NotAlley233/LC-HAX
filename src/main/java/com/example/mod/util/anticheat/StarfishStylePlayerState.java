package com.example.mod.util.anticheat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Per-target-player state aligned with Starfish anticheat-0-2-1.js PlayerData + violation tracking.
 */
public class StarfishStylePlayerState {

    public double horizontalSpeedBps;
    public double verticalSpeedBps;

    private long noSlowStartTimeMs;
    private boolean noSlowActive;

    private final ArrayDeque<Long> shiftStartTimesMs = new ArrayDeque<>();
    private boolean wasSneaking;

    public static final class HeightSample {
        public final double y;
        public final long timeMs;

        public HeightSample(double y, long timeMs) {
            this.y = y;
            this.timeMs = timeMs;
        }
    }

    private final ArrayList<HeightSample> towerHeightHistory = new ArrayList<>();
    private long towerLastResetMs;

    private long lastDamagedMs;
    private int lastHurtTime;

    public final Map<String, Integer> violations = new HashMap<>();
    public final Map<String, Long> lastAlertMs = new HashMap<>();

    public StarfishStylePlayerState() {
        towerLastResetMs = System.currentTimeMillis();
        for (String c : AntiCheatCheckIds.ALL) {
            violations.put(c, 0);
            lastAlertMs.put(c, 0L);
        }
    }

    public void update(EntityPlayer p, long nowMs) {
        double dx = p.posX - p.prevPosX;
        double dy = p.posY - p.prevPosY;
        double dz = p.posZ - p.prevPosZ;
        horizontalSpeedBps = Math.sqrt(dx * dx + dz * dz) * 20.0D;
        verticalSpeedBps = dy * 20.0D;

        boolean sneak = p.isSneaking();
        if (sneak && !wasSneaking) {
            shiftStartTimesMs.addLast(nowMs);
            while (shiftStartTimesMs.size() > 50) {
                shiftStartTimesMs.removeFirst();
            }
        }
        wasSneaking = sneak;

        Iterator<Long> it = shiftStartTimesMs.iterator();
        while (it.hasNext()) {
            if (nowMs - it.next() > 2000L) {
                it.remove();
            }
        }

        int ht = p.hurtTime;
        if (ht > 0 && lastHurtTime == 0) {
            lastDamagedMs = nowMs;
        }
        lastHurtTime = ht;
    }

    public int shiftStartsInWindow() {
        return shiftStartTimesMs.size();
    }

    public boolean isNoSlowActive() {
        return noSlowActive;
    }

    public long getNoSlowStartTimeMs() {
        return noSlowStartTimeMs;
    }

    public void setNoSlowActive(boolean active) {
        this.noSlowActive = active;
    }

    public void setNoSlowStartTimeMs(long noSlowStartTimeMs) {
        this.noSlowStartTimeMs = noSlowStartTimeMs;
    }

    public void clearNoSlowTracking() {
        noSlowActive = false;
        noSlowStartTimeMs = 0L;
    }

    public ArrayList<HeightSample> getTowerHeightHistory() {
        return towerHeightHistory;
    }

    public long getTowerLastResetMs() {
        return towerLastResetMs;
    }

    public void setTowerLastResetMs(long towerLastResetMs) {
        this.towerLastResetMs = towerLastResetMs;
    }

    public long getLastDamagedMs() {
        return lastDamagedMs;
    }

    public boolean hasJumpBoost(EntityPlayer p) {
        PotionEffect e = p.getActivePotionEffect(Potion.jump);
        return e != null;
    }

    public void addViolation(String check, int amount) {
        violations.put(check, violations.getOrDefault(check, 0) + amount);
    }

    public void reduceViolation(String check, int amount) {
        int v = violations.getOrDefault(check, 0);
        violations.put(check, Math.max(0, v - amount));
    }

    public void markAlert(String check, long nowMs) {
        lastAlertMs.put(check, nowMs);
    }

    public boolean shouldAlert(String check, int vlThreshold, long cooldownMs, long nowMs) {
        int v = violations.getOrDefault(check, 0);
        if (v < vlThreshold) {
            return false;
        }
        long last = lastAlertMs.getOrDefault(check, 0L);
        return nowMs - last > cooldownMs;
    }

    public void clearViolationsAndAlerts() {
        for (String c : AntiCheatCheckIds.ALL) {
            violations.put(c, 0);
            lastAlertMs.put(c, 0L);
        }
        noSlowActive = false;
        shiftStartTimesMs.clear();
        towerHeightHistory.clear();
        wasSneaking = false;
    }
}
