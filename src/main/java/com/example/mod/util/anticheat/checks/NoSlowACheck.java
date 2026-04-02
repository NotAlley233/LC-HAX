package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.anticheat.AntiCheatCheckIds;
import com.example.mod.util.anticheat.AntiCheatItems;
import com.example.mod.util.anticheat.StarfishStylePlayerState;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Starfish NoSlowA: sprinting while using slowdown items for 500ms+.
 */
public class NoSlowACheck {

    public void check(EntityPlayer player, AntiCheat antiCheat, StarfishStylePlayerState state, long nowMs) {
        if (!antiCheat.isDetectNoSlow()) {
            state.clearNoSlowTracking();
            return;
        }

        boolean isCurrentlyNoSlow = AntiCheatItems.isUsingSlowdownItem(player) && player.isSprinting();

        if (isCurrentlyNoSlow) {
            if (!state.isNoSlowActive()) {
                state.setNoSlowStartTimeMs(nowMs);
                state.setNoSlowActive(true);
            }
            long duration = nowMs - state.getNoSlowStartTimeMs();
            if (duration >= 500L) {
                state.addViolation(AntiCheatCheckIds.NO_SLOW_A, 2);
            }
        } else {
            state.setNoSlowActive(false);
            state.setNoSlowStartTimeMs(0L);
            state.reduceViolation(AntiCheatCheckIds.NO_SLOW_A, 1);
        }
    }
}
