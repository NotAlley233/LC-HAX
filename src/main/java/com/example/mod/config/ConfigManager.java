package com.example.mod.config;

import com.example.mod.core.ModLogger;
import com.example.mod.module.KeyBindingManager;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.Property;
import com.example.mod.property.PropertyManager;
import com.example.mod.core.ToggleAnnouncements;
import com.google.gson.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager {
    private final Path configDir;
    private final ModuleManager moduleManager;
    private final PropertyManager propertyManager;
    private final KeyBindingManager keyBindingManager;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "ConfigIO-Thread");
        t.setDaemon(true);
        return t;
    });

    public ConfigManager(Path baseDir, ModuleManager moduleManager, PropertyManager propertyManager, KeyBindingManager keyBindingManager) {
        this.configDir = baseDir.resolve("keymaps");
        this.moduleManager = moduleManager;
        this.propertyManager = propertyManager;
        this.keyBindingManager = keyBindingManager;
        try {
            Files.createDirectories(this.configDir);
        } catch (IOException e) {
            ModLogger.error("Failed to create config directory", e);
        }
    }

    public CompletableFuture<Void> save(String profile) {
        return CompletableFuture.runAsync(() -> {
            JsonObject root = new JsonObject();
            root.addProperty("version", 1);

            JsonObject metadata = new JsonObject();
            metadata.addProperty("createdAt", Instant.now().toString());
            metadata.addProperty("profile", profile);
            root.add("metadata", metadata);

            JsonObject bindingsObj = new JsonObject();
            for (Map.Entry<String, Integer> entry : keyBindingManager.getAllBindings().entrySet()) {
                bindingsObj.addProperty(entry.getKey(), entry.getValue());
            }
            root.add("bindings", bindingsObj);

            JsonObject modulesObj = new JsonObject();
            for (Module m : moduleManager.all()) {
                JsonObject modJson = new JsonObject();
                modJson.addProperty("enabled", m.enabled());

                if (propertyManager.getProperties(m) != null) {
                    for (Property<?> prop : propertyManager.getProperties(m)) {
                        modJson.addProperty(prop.getName(), String.valueOf(prop.getValue()));
                    }
                }
                modulesObj.add(m.name(), modJson);
            }
            root.add("modules", modulesObj);

            Path profilePath = configDir.resolve(profile + ".json");
            Path tempPath = configDir.resolve(profile + ".json.tmp");

            try {
                try (Writer writer = Files.newBufferedWriter(tempPath)) {
                    GSON.toJson(root, writer);
                }
                Files.move(tempPath, profilePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                ModLogger.info("Successfully saved profile: " + profile);
            } catch (IOException e) {
                ModLogger.error("Failed to save profile: " + profile, e);
                try { Files.deleteIfExists(tempPath); } catch (IOException ignored) {}
                throw new RuntimeException("Save failed", e);
            }
        }, ioExecutor);
    }

    public CompletableFuture<Void> load(String profile) {
        return CompletableFuture.runAsync(() -> {
            Path profilePath = configDir.resolve(profile + ".json");
            if (!Files.exists(profilePath)) {
                throw new RuntimeException("ERR_PROFILE_NOT_FOUND");
            }

            try (Reader reader = Files.newBufferedReader(profilePath)) {
                JsonElement element = new JsonParser().parse(reader);
                if (!element.isJsonObject()) throw new RuntimeException("Invalid JSON format");
                JsonObject root = element.getAsJsonObject();

                // Validate schema manually
                if (!root.has("version") || !root.has("bindings") || !root.has("modules")) {
                    throw new RuntimeException("ERR_SCHEMA_VALIDATION_FAILED");
                }

                // Parse into memory first to avoid partial state changes
                Map<String, Integer> newBindings = new HashMap<>();
                JsonObject bindingsObj = root.getAsJsonObject("bindings");
                for (Map.Entry<String, JsonElement> entry : bindingsObj.entrySet()) {
                    newBindings.put(entry.getKey(), entry.getValue().getAsInt());
                }

                JsonObject modulesObj = root.getAsJsonObject("modules");
                
                // Now apply changes safely
                keyBindingManager.loadFromMap(newBindings);

                ToggleAnnouncements.pushSilent();
                try {
                    for (Module m : moduleManager.all()) {
                        if (modulesObj.has(m.name())) {
                            JsonObject modJson = modulesObj.getAsJsonObject(m.name());
                            if (modJson.has("enabled")) {
                                m.setEnabled(modJson.get("enabled").getAsBoolean());
                            }

                            if (propertyManager.getProperties(m) != null) {
                                for (Property<?> prop : propertyManager.getProperties(m)) {
                                    if (modJson.has(prop.getName())) {
                                        try {
                                            prop.parseString(modJson.get(prop.getName()).getAsString());
                                        } catch (Exception e) {
                                            ModLogger.error("Failed to parse property " + prop.getName() + " for module " + m.name(), e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    ToggleAnnouncements.popSilent();
                }
                ModLogger.info("Successfully loaded profile: " + profile);
            } catch (Exception e) {
                ModLogger.error("Failed to load profile: " + profile, e);
                throw new RuntimeException("Load failed: " + e.getMessage(), e);
            }
        }, ioExecutor);
    }

    public CompletableFuture<List<String>> list() {
        return CompletableFuture.supplyAsync(() -> {
            try (Stream<Path> stream = Files.list(configDir)) {
                return stream
                        .filter(p -> p.toString().endsWith(".json"))
                        .map(p -> p.getFileName().toString().replace(".json", ""))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                ModLogger.error("Failed to list profiles", e);
                return new ArrayList<>();
            }
        }, ioExecutor);
    }

    public CompletableFuture<Boolean> delete(String profile) {
        return CompletableFuture.supplyAsync(() -> {
            Path profilePath = configDir.resolve(profile + ".json");
            try {
                return Files.deleteIfExists(profilePath);
            } catch (IOException e) {
                ModLogger.error("Failed to delete profile: " + profile, e);
                return false;
            }
        }, ioExecutor);
    }
}
