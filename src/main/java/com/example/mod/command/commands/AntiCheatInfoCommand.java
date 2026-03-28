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
        ChatUtil.sendFormatted("§bAntiCheat §7(" + (antiCheat.enabled() ? "§aon" : "§coff") + "§7)");
        ChatUtil.sendFormatted("§7violationLevel: §f" + antiCheat.getViolationLevel());
        ChatUtil.sendFormatted("§7detectAutoBlock: §f" + antiCheat.isDetectAutoBlock());
        ChatUtil.sendFormatted("§7detectNoSlow: §f" + antiCheat.isDetectNoSlow());
        ChatUtil.sendFormatted("§7detectLegitScaffold: §f" + antiCheat.isDetectLegitScaffold());
        ChatUtil.sendFormatted("§7detectKillaura: §f" + antiCheat.isDetectKillaura());
        ChatUtil.sendFormatted("§7flagPingSound: §f" + antiCheat.isFlagPingSound());
        ChatUtil.sendFormatted("§7flagWDRButton: §f" + antiCheat.isFlagWDRButton());
        ChatUtil.sendFormatted("§7debugMessages: §f" + antiCheat.isDebugMessages());
        ChatUtil.sendFormatted("§7trackedPlayers: §f" + antiCheat.getTrackedPlayers());
        ChatUtil.sendFormatted("§7flags: §fAutoBlock=" + antiCheat.getFlagCount("AutoBlock")
                + " §fNoSlow=" + antiCheat.getFlagCount("NoSlow")
                + " §fLegitScaffold=" + antiCheat.getFlagCount("Legit Scaffold")
                + " §fKillaura=" + antiCheat.getFlagCount("Killaura")
                + " §fTotal=" + antiCheat.getTotalFlags());
    }
}
