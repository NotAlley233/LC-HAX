package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.config.ConfigStore;
import com.example.mod.util.ChatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefixCommand implements Command {
    private final ConfigStore configStore;

    public PrefixCommand(ConfigStore configStore) {
        this.configStore = configStore;
    }

    @Override public List<String> names() { return Arrays.asList("prefix"); }

    @Override
    public void runCommand(ArrayList<String> args) {
        // args[0] is the command root, like myau
        if (args.size() < 2) {
            String cur = ChatUtil.getPrefixText();
            if (cur == null || cur.trim().isEmpty()) {
                ChatUtil.info("Prefix text is empty.");
            } else {
                ChatUtil.info("Prefix: " + cur);
            }
            return;
        }

        // Prefix enable/disable is controlled via `.t prefix`

        String text = String.join(" ", args.subList(1, args.size()));
        configStore.setPrefixText(text);
        ChatUtil.setPrefixText(text);
        ChatUtil.info("Prefix set to: " + text);
    }
}

