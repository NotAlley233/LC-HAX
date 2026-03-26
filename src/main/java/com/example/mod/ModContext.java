package com.example.mod;

import com.example.mod.command.CommandManager;
import com.example.mod.module.ModuleManager;

public final class ModContext {
    private static volatile CommandManager commandManager;
    private static volatile ModuleManager moduleManager;

    private ModContext() {}

    public static void setCommandManager(CommandManager manager) {
        commandManager = manager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static void setModuleManager(ModuleManager manager) {
        moduleManager = manager;
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }
}

