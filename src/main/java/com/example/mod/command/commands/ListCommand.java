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
        for (Module m : moduleManager.all()) {
            ChatUtil.info("  " + m.name() + " = " + (m.enabled() ? "ON" : "OFF") + " - " + m.description());
        }
    }
}
