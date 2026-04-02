package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.ChatUtil;
import com.example.mod.util.anticheat.AntiCheatCheckIds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AntiCheatInfoCommand implements Command {
    private final AntiCheat antiCheat;

    public AntiCheatInfoCommand(AntiCheat antiCheat) {
        this.antiCheat = antiCheat;
    }

    @Override
    public List<String> names() {
        return Arrays.asList("acinfo");
    }

    @Override
    public void runCommand(ArrayList<String> args) {
        if (antiCheat == null) {
            ChatUtil.error("AntiCheat 未初始化");
            return;
        }
        ChatUtil.sendPrefixedFormatted("§bAntiCheat §7(" + (antiCheat.enabled() ? "§aON" : "§cOFF") + "§7) §8Starfish-style");
        ChatUtil.sendPrefixedFormatted("§7NoSlowA §fdetect=§f" + antiCheat.isDetectNoSlow()
                + " §7vl=§f" + antiCheat.getVlNoSlow() + " §7cd=§f" + antiCheat.getCooldownNoSlowMs() + "ms §7sound=§f" + antiCheat.isSoundNoSlow());
        ChatUtil.sendPrefixedFormatted("§7AutoBlockA §fdetect=§f" + antiCheat.isDetectAutoBlock()
                + " §7vl=§f" + antiCheat.getVlAutoBlock() + " §7cd=§f" + antiCheat.getCooldownAutoBlockMs() + "ms §7sound=§f" + antiCheat.isSoundAutoBlock());
        ChatUtil.sendPrefixedFormatted("§7EagleA §fdetect=§f" + antiCheat.isDetectEagle()
                + " §7vl=§f" + antiCheat.getVlEagle() + " §7cd=§f" + antiCheat.getCooldownEagleMs() + "ms §7sound=§f" + antiCheat.isSoundEagle());
        ChatUtil.sendPrefixedFormatted("§7ScaffoldA §fdetect=§f" + antiCheat.isDetectScaffold()
                + " §7vl=§f" + antiCheat.getVlScaffold() + " §7cd=§f" + antiCheat.getCooldownScaffoldMs() + "ms §7sound=§f" + antiCheat.isSoundScaffold());
        ChatUtil.sendPrefixedFormatted("§7TowerA §fdetect=§f" + antiCheat.isDetectTower()
                + " §7vl=§f" + antiCheat.getVlTower() + " §7cd=§f" + antiCheat.getCooldownTowerMs() + "ms §7sound=§f" + antiCheat.isSoundTower());
        ChatUtil.sendPrefixedFormatted("§7KillauraB §fdetect=§f" + antiCheat.isDetectKillaura()
                + " §7vl=§f" + antiCheat.getVlKillaura() + " §7cd=§f" + antiCheat.getCooldownKillauraMs() + "ms §7sound=§f" + antiCheat.isSoundKillaura());
        ChatUtil.sendPrefixedFormatted("§7flagWDRButton: §f" + antiCheat.isFlagWDRButton());
        ChatUtil.sendPrefixedFormatted("§7debugMessages: §f" + antiCheat.isDebugMessages());
        ChatUtil.sendPrefixedFormatted("§7trackedPlayers: §f" + antiCheat.getTrackedPlayers());
        ChatUtil.sendPrefixedFormatted("§7flags: §f"
                + AntiCheatCheckIds.NO_SLOW_A + "=" + antiCheat.getFlagCount(AntiCheatCheckIds.NO_SLOW_A) + " "
                + AntiCheatCheckIds.AUTO_BLOCK_A + "=" + antiCheat.getFlagCount(AntiCheatCheckIds.AUTO_BLOCK_A) + " "
                + AntiCheatCheckIds.EAGLE_A + "=" + antiCheat.getFlagCount(AntiCheatCheckIds.EAGLE_A) + " "
                + AntiCheatCheckIds.SCAFFOLD_A + "=" + antiCheat.getFlagCount(AntiCheatCheckIds.SCAFFOLD_A) + " "
                + AntiCheatCheckIds.TOWER_A + "=" + antiCheat.getFlagCount(AntiCheatCheckIds.TOWER_A) + " "
                + AntiCheatCheckIds.KILLAURA_B + "=" + antiCheat.getFlagCount(AntiCheatCheckIds.KILLAURA_B)
                + " §7Total=§f" + antiCheat.getTotalFlags());
    }
}
