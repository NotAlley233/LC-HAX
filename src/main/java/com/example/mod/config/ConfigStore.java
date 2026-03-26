package com.example.mod.config;

import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.PropertyManager;
import com.example.mod.property.Property;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.example.mod.core.ModLogger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigStore {
    private final Path path;
    private JsonObject configObj = new JsonObject();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ConfigStore(Path path) {
        this.path = path;
        reload();
    }

    public static Path defaultConfigPath(String fileName) {
        return Paths.get("run").resolve(fileName);
    }

    public void loadInto(ModuleManager moduleManager, PropertyManager propertyManager) {
        for (Module m : moduleManager.all()) {
            String moduleKey = m.name();
            if (configObj.has(moduleKey)) {
                JsonObject modJson = configObj.getAsJsonObject(moduleKey);
                
                // Load Module Enabled State
                if (modJson.has("enabled")) {
                    m.setEnabled(modJson.get("enabled").getAsBoolean());
                }

                // Load properties
                if (propertyManager.getProperties(m) != null) {
                    for (Property<?> prop : propertyManager.getProperties(m)) {
                        if (modJson.has(prop.getName())) {
                            try {
                                String valStr = modJson.get(prop.getName()).getAsString();
                                prop.parseString(valStr);
                            } catch (Exception e) {
                                ModLogger.error("Failed to parse property " + prop.getName() + " for module " + m.name(), e);
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveFrom(ModuleManager moduleManager, PropertyManager propertyManager) {
        for (Module m : moduleManager.all()) {
            JsonObject modJson = new JsonObject();
            modJson.addProperty("enabled", m.enabled());

            if (propertyManager.getProperties(m) != null) {
                for (Property<?> prop : propertyManager.getProperties(m)) {
                    modJson.addProperty(prop.getName(), String.valueOf(prop.getValue()));
                }
            }

            configObj.add(m.name(), modJson);
        }

        persist();
    }

    public void reload() {
        if (!Files.exists(path)) return;
        try (Reader reader = Files.newBufferedReader(path)) {
            JsonElement element = new JsonParser().parse(reader);
            if (element.isJsonObject()) {
                configObj = element.getAsJsonObject();
            }
        } catch (IOException e) {
            ModLogger.error("Failed to read config", e);
        }
    }

    private void persist() {
        try {
            Path parent = path.getParent();
            if (parent != null) Files.createDirectories(parent);
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(configObj, writer);
            }
        } catch (IOException e) {
            ModLogger.error("Failed to save config", e);
        }
    }
}
