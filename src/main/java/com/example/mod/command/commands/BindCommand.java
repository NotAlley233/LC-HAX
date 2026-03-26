package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.module.KeyBindingManager;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.util.ChatUtil;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BindCommand implements Command {
    private final ModuleManager moduleManager;
    private final KeyBindingManager keyBindingManager;

    public BindCommand(ModuleManager moduleManager, KeyBindingManager keyBindingManager) {
        this.moduleManager = moduleManager;
        this.keyBindingManager = keyBindingManager;
    }

    @Override
    public List<String> names() {
        return Arrays.asList("bind", "b");
    }

    @Override
    public void runCommand(ArrayList<String> args) {
        if (args.size() < 2) {
            ChatUtil.error("Usage: .bind <module> <key> | .bind -l | .bind -u <module>");
            return;
        }

        String actionOrModule = args.get(1);

        if (actionOrModule.equalsIgnoreCase("-l")) {
            Map<String, Integer> bindings = keyBindingManager.getAllBindings();
            if (bindings.isEmpty()) {
                ChatUtil.sendFormatted("&eNo keybindings found.");
                return;
            }
            ChatUtil.sendFormatted("&aCurrent Keybindings:");
            for (Map.Entry<String, Integer> entry : bindings.entrySet()) {
                String keyName = Keyboard.getKeyName(entry.getValue());
                ChatUtil.sendFormatted(String.format("&7- &b%s&7: &e%s", entry.getKey(), keyName));
            }
            return;
        }

        if (actionOrModule.equalsIgnoreCase("-u")) {
            if (args.size() < 3) {
                ChatUtil.error("Usage: .bind -u <module>");
                return;
            }
            String moduleName = args.get(2);
            if (keyBindingManager.unbind(moduleName)) {
                ChatUtil.sendFormatted("&aUnbound module &e" + moduleName + "&a.");
            } else {
                ChatUtil.error("Module &e" + moduleName + "&c is not bound to any key.");
            }
            return;
        }

        // Bind <module> <key>
        if (args.size() < 3) {
            ChatUtil.error("Usage: .bind <module> <key>");
            return;
        }

        String moduleName = actionOrModule;
        Module m = moduleManager.get(moduleName);
        if (m == null) {
            ChatUtil.error("Module not found: " + moduleName);
            return;
        }

        String keyName = args.get(2).toUpperCase();
        int keyCode = Keyboard.getKeyIndex(keyName);
        if (keyCode == Keyboard.KEY_NONE) {
            ChatUtil.error("Invalid key name: " + keyName);
            return;
        }

        boolean force = args.size() >= 4 && args.get(3).equalsIgnoreCase("-f");

        String existingModule = keyBindingManager.getBoundModule(keyCode);
        if (existingModule != null && !existingModule.equalsIgnoreCase(moduleName) && !force) {
            ChatUtil.error("Key &e" + keyName + "&c is already bound to &e" + existingModule + "&c.");
            ChatUtil.error("Use &e.bind " + moduleName + " " + keyName + " -f&c to override.");
            return;
        }

        keyBindingManager.bind(moduleName, keyCode, true);
        ChatUtil.sendFormatted("&aBound &e" + m.name() + "&a to key &e" + keyName + "&a.");
    }
}
