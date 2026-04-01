package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.ChatUtil;

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
        ChatUtil.sendPrefixedFormatted("§bAntiCheat §7(" + (antiCheat.enabled() ? "§aON" : "§cOFF") + "§7)");
        ChatUtil.sendPrefixedFormatted("§7violationLevel: §f" + antiCheat.getViolationLevel());
        ChatUtil.sendPrefixedFormatted("§7detectAutoBlock: §f" + antiCheat.isDetectAutoBlock());
        ChatUtil.sendPrefixedFormatted("§7detectNoSlow: §f" + antiCheat.isDetectNoSlow());
        ChatUtil.sendPrefixedFormatted("§7detectLegitScaffold: §f" + antiCheat.isDetectLegitScaffold());
        ChatUtil.sendPrefixedFormatted("§7detectKillaura: §f" + antiCheat.isDetectKillaura());
        ChatUtil.sendPrefixedFormatted("§7flagPingSound: §f" + antiCheat.isFlagPingSound());
        ChatUtil.sendPrefixedFormatted("§7flagWDRButton: §f" + antiCheat.isFlagWDRButton());
        ChatUtil.sendPrefixedFormatted("§7debugMessages: §f" + antiCheat.isDebugMessages());
        ChatUtil.sendPrefixedFormatted("§7trackedPlayers: §f" + antiCheat.getTrackedPlayers());
        ChatUtil.sendPrefixedFormatted("§7flags: §fAutoBlock=" + antiCheat.getFlagCount("AutoBlock")
                + " §fNoSlow=" + antiCheat.getFlagCount("NoSlow")
                + " §fLegitScaffold=" + antiCheat.getFlagCount("Legit Scaffold")
                + " §fKillaura=" + antiCheat.getFlagCount("Killaura")
                + " §fTotal=" + antiCheat.getTotalFlags());
    }
}
