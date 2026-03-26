package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.config.ConfigManager;
import com.example.mod.util.ChatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigCommand implements Command {
    private final ConfigManager configManager;

    public ConfigCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public List<String> names() {
        return Arrays.asList("c", "config");
    }

    @Override
    public void runCommand(ArrayList<String> args) {
        if (args.size() < 2) {
            ChatUtil.error("Usage: .c <save|load|list|delete> [profile]");
            return;
        }

        String action = args.get(1).toLowerCase();

        switch (action) {
            case "save":
                if (args.size() < 3) {
                    ChatUtil.error("Usage: .c save <profile>");
                    return;
                }
                String saveProfile = args.get(2);
                configManager.save(saveProfile).whenComplete((v, ex) -> {
                    if (ex != null) {
                        ChatUtil.error("Failed to save profile '&e" + saveProfile + "&c'. Check logs.");
                    } else {
                        ChatUtil.sendFormatted("&aSuccessfully saved profile '&e" + saveProfile + "&a'.");
                    }
                });
                break;

            case "load":
                if (args.size() < 3) {
                    ChatUtil.error("Usage: .c load <profile>");
                    return;
                }
                String loadProfile = args.get(2);
                configManager.load(loadProfile).whenComplete((v, ex) -> {
                    if (ex != null) {
                        if (ex.getMessage() != null && ex.getMessage().contains("ERR_PROFILE_NOT_FOUND")) {
                            ChatUtil.error("[ERROR] Profile '&e" + loadProfile + "&c' not found. (ERR_PROFILE_NOT_FOUND)");
                        } else if (ex.getMessage() != null && ex.getMessage().contains("ERR_SCHEMA_VALIDATION_FAILED")) {
                            ChatUtil.error("[ERROR] Profile '&e" + loadProfile + "&c' is corrupted or invalid. (ERR_SCHEMA_VALIDATION_FAILED)");
                        } else {
                            ChatUtil.error("[ERROR] Failed to load profile '&e" + loadProfile + "&c'. (ERR_LOAD_FAILED)");
                        }
                    } else {
                        ChatUtil.sendFormatted("&aSuccessfully loaded profile '&e" + loadProfile + "&a'.");
                    }
                });
                break;

            case "list":
                configManager.list().whenComplete((profiles, ex) -> {
                    if (ex != null) {
                        ChatUtil.error("Failed to list profiles.");
                    } else if (profiles.isEmpty()) {
                        ChatUtil.sendFormatted("&eNo profiles found.");
                    } else {
                        ChatUtil.sendFormatted("&aAvailable Profiles:");
                        for (String profile : profiles) {
                            ChatUtil.sendFormatted("&7- &b" + profile);
                        }
                    }
                });
                break;

            case "delete":
                if (args.size() < 3) {
                    ChatUtil.error("Usage: .c delete <profile>");
                    return;
                }
                String deleteProfile = args.get(2);
                configManager.delete(deleteProfile).whenComplete((success, ex) -> {
                    if (ex != null || !success) {
                        ChatUtil.error("Failed to delete profile '&e" + deleteProfile + "&c'.");
                    } else {
                        ChatUtil.sendFormatted("&aSuccessfully deleted profile '&e" + deleteProfile + "&a'.");
                    }
                });
                break;

            default:
                ChatUtil.error("Unknown action: " + action);
                ChatUtil.error("Usage: .c <save|load|list|delete> [profile]");
                break;
        }
    }
}
