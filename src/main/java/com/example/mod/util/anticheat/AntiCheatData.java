package com.example.mod.util.anticheat;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.anticheat.checks.AutoBlockCheck;
import com.example.mod.util.anticheat.checks.KillauraBCheck;
import com.example.mod.util.anticheat.checks.LegitScaffoldCheck;
import com.example.mod.util.anticheat.checks.NoSlowCheck;
import net.minecraft.entity.player.EntityPlayer;

public class AntiCheatData {
    public final PlayerData playerData = new PlayerData();
    public final AutoBlockCheck autoBlockCheck = new AutoBlockCheck();
    public final NoSlowCheck noSlowCheck = new NoSlowCheck();
    public final LegitScaffoldCheck legitScaffoldCheck = new LegitScaffoldCheck();
    public final KillauraBCheck killauraBCheck = new KillauraBCheck();

    public void anticheatCheck(EntityPlayer player, AntiCheat antiCheat) {
        playerData.update(player);
        autoBlockCheck.anticheatCheck(player, antiCheat);
        noSlowCheck.anticheatCheck(player, antiCheat);
        legitScaffoldCheck.anticheatCheck(player, antiCheat);
        killauraBCheck.anticheatCheck(player, antiCheat);
    }

    public boolean failedAutoBlock() {
        return autoBlockCheck.failedAutoBlock();
    }

    public boolean failedNoSlow() {
        return noSlowCheck.failedNoSlow();
    }

    public boolean failedLegitScaffold() {
        return legitScaffoldCheck.failedLegitScaffold();
    }

    public boolean failedKillauraB() {
        return killauraBCheck.failedKillauraB();
    }
}
