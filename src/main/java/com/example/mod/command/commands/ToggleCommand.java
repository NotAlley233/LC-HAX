package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.config.ConfigStore;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.util.ChatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ToggleCommand implements Command {
    private final ModuleManager moduleManager;
    private final ConfigStore configStore;

    public ToggleCommand(ModuleManager moduleManager, ConfigStore configStore) {
        this.moduleManager = moduleManager;
        this.configStore = configStore;
    }

    @Override public List<String> names() { return Arrays.asList("toggle", "t"); }

    @Override
    public void runCommand(ArrayList<String> args) {
        // args[0] is the command root, like myau
        if (args.size() < 2) {
            ChatUtil.error("Usage: ." + args.get(0).toLowerCase(Locale.ROOT) + " <module>");
            return;
        }

        String moduleName = args.get(1).toLowerCase(Locale.ROOT);
        Module m = moduleManager.get(moduleName);
        if (m == null) {
            ChatUtil.error("Module not found (" + args.get(1) + ")");
            return;
        }

        boolean changed = true;
        if (args.size() >= 3) {
            String v = args.get(2);
            if (equalsAnyIgnoreCase(v, "true", "on", "1")) {
                changed = !m.enabled();
            } else if (equalsAnyIgnoreCase(v, "false", "off", "0")) {
                changed = m.enabled();
            }
        }

        if (!changed) return;

        m.setEnabled(!m.enabled());
        configStore.saveFrom(moduleManager);
        ChatUtil.sendFormatted(String.format("%s: %s&r", m.name(), m.enabled() ? "&a&lON" : "&c&lOFF"));
    }

    private static boolean equalsAnyIgnoreCase(String s, String... options) {
        if (s == null) return false;
        for (String o : options) {
            if (s.equalsIgnoreCase(o)) return true;
        }
        return false;
    }
}
