package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.util.ChatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListCommand implements Command {
    private final ModuleManager moduleManager;

    public ListCommand(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    @Override public List<String> names() { return Arrays.asList("list"); }

    @Override
    public void runCommand(ArrayList<String> args) {
        ChatUtil.info("Modules:");
        List<Module> modules = new ArrayList<>(moduleManager.all());
        modules.sort((a, b) -> a.name().compareToIgnoreCase(b.name()));
        for (Module m : modules) {
            String statusColor = m.enabled() ? "§aon" : "§coff";
            ChatUtil.sendFormatted(m.name() + "(" + statusColor + "§r)");
        }
    }
}
