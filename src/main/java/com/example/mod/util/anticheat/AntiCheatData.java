package com.example.mod.util.anticheat;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.anticheat.checks.AutoBlockACheck;
import com.example.mod.util.anticheat.checks.EagleACheck;
import com.example.mod.util.anticheat.checks.KillauraBCheck;
import com.example.mod.util.anticheat.checks.NoSlowACheck;
import com.example.mod.util.anticheat.checks.ScaffoldACheck;
import com.example.mod.util.anticheat.checks.TowerACheck;
import net.minecraft.entity.player.EntityPlayer;

public class AntiCheatData {
    public final StarfishStylePlayerState starfishState = new StarfishStylePlayerState();
    public final NoSlowACheck noSlowACheck = new NoSlowACheck();
    public final AutoBlockACheck autoBlockACheck = new AutoBlockACheck();
    public final EagleACheck eagleACheck = new EagleACheck();
    public final ScaffoldACheck scaffoldACheck = new ScaffoldACheck();
    public final TowerACheck towerACheck = new TowerACheck();
    public final KillauraBCheck killauraBCheck = new KillauraBCheck();

    public void runChecks(EntityPlayer player, AntiCheat antiCheat, long nowMs) {
        starfishState.update(player, nowMs);
        noSlowACheck.check(player, antiCheat, starfishState, nowMs);
        autoBlockACheck.check(player, antiCheat, starfishState, nowMs);
        eagleACheck.check(player, antiCheat, starfishState);
        scaffoldACheck.check(player, antiCheat, starfishState);
        towerACheck.check(player, antiCheat, starfishState, nowMs);
        killauraBCheck.check(player, antiCheat, starfishState);
    }
}
