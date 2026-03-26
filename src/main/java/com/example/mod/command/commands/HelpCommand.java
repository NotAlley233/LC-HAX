package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.command.CommandManager;
import com.example.mod.util.ChatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommand implements Command {
    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override public List<String> names() { return Arrays.asList("help", "?"); }

    @Override
    public void runCommand(ArrayList<String> args) {
        ChatUtil.info("Commands:");
        for (Command c : manager.all()) {
            ChatUtil.info("  ." + c.names().get(0));
        }
    }
}
