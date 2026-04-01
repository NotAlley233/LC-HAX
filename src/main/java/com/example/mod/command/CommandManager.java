package com.example.mod.command;

import com.example.mod.config.ConfigStore;
import com.example.mod.module.ModuleManager;
import com.example.mod.util.ChatUtil;

import java.util.*;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();

    private final ModuleManager moduleManager;
    private final ConfigStore configStore;

    public CommandManager(ModuleManager moduleManager, ConfigStore configStore) {
        this.moduleManager = moduleManager;
        this.configStore = configStore;
    }

    public void register(Command cmd) {
        commands.add(cmd);
    }

    public List<Command> all() {
        return Collections.unmodifiableList(commands);
    }

    public boolean isTypingCommand(String string) {
        if (string == null || string.length() < 2) return false;
        return string.charAt(0) == '.' && Character.isLetterOrDigit(string.charAt(1));
    }

    /**
     * myau-compatible entry: accepts the full message including leading dot.
     */
    public void handleCommand(String string) {
        if (string == null || string.length() < 2) {
            ChatUtil.sendPrefixedFormatted("&cUnknown command&r");
            return;
        }

        String body = string.substring(1).trim();
        if (body.isEmpty()) {
            ChatUtil.sendPrefixedFormatted("&cUnknown command&r");
            return;
        }

        List<String> params = Arrays.asList(body.split("\\s+"));
        if (params.isEmpty() || params.get(0).isEmpty()) {
            ChatUtil.sendPrefixedFormatted("&cUnknown command&r");
            return;
        }

        ArrayList<String> args = new ArrayList<>(params);
        String root = params.get(0);
        for (Command command : commands) {
            for (String name : command.names()) {
                if (root.equalsIgnoreCase(name)) {
                    try {
                        command.runCommand(args);
                    } catch (Throwable t) {
                        ChatUtil.sendPrefixedFormatted(String.format("&cCommand failed (&o&e%s&c)&r", root));
                    }
                    return;
                }
            }
        }

        ChatUtil.sendPrefixedFormatted(String.format("&cUnknown command (&o&e%s&c)&r", root));
    }

    public ModuleManager modules() {
        return moduleManager;
    }

    public ConfigStore config() {
        return configStore;
    }
}
