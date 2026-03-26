package com.example.mod;

import com.example.mod.command.CommandManager;

public final class ModContext {
    private static volatile CommandManager commandManager;

    private ModContext() {}

    public static void setCommandManager(CommandManager manager) {
        commandManager = manager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}

