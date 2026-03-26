package com.example.mod.config;

import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigStore {
    public static final String PREFIX_TEXT_KEY = "ui.prefixText";
    public static final String PREFIX_ENABLED_KEY = "ui.prefixEnabled";

    private final Path path;
    private final Properties properties = new Properties();

    public ConfigStore(Path path) {
        this.path = path;
        reload();
    }

    public static Path defaultConfigPath(String fileName) {
        return Paths.get("run").resolve(fileName);
    }

    public void loadInto(ModuleManager moduleManager) {
        for (Module m : moduleManager.all()) {
            String key = keyFor(m.name());
            String v = properties.getProperty(key);
            if (v == null) continue;
            m.setEnabled(Boolean.parseBoolean(v));
        }

        // Backward-compatible: if old ui.prefixEnabled exists, map it onto the prefix module.
        if (properties.getProperty(PREFIX_ENABLED_KEY) != null) {
            Module prefix = moduleManager.get("prefix");
            if (prefix != null) {
                prefix.setEnabled(isPrefixEnabled());
            }
        }
    }

    public void saveFrom(ModuleManager moduleManager) {
        for (Module m : moduleManager.all()) {
            properties.setProperty(keyFor(m.name()), Boolean.toString(m.enabled()));
        }

        persist();
    }

    public String getPrefixText() {
        return getString(PREFIX_TEXT_KEY, "prefix");
    }

    public void setPrefixText(String text) {
        if (text == null || text.trim().isEmpty()) {
            properties.remove(PREFIX_TEXT_KEY);
        } else {
            properties.setProperty(PREFIX_TEXT_KEY, text);
        }
        persist();
    }

    public boolean isPrefixEnabled() {
        return getBoolean(PREFIX_ENABLED_KEY, true);
    }

    public void setPrefixEnabled(boolean enabled) {
        properties.setProperty(PREFIX_ENABLED_KEY, Boolean.toString(enabled));
        persist();
    }

    public String getString(String key, String defaultValue) {
        String v = properties.getProperty(key);
        return v == null ? defaultValue : v;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String v = properties.getProperty(key);
        return v == null ? defaultValue : Boolean.parseBoolean(v);
    }

    public void setString(String key, String value) {
        if (value == null) properties.remove(key);
        else properties.setProperty(key, value);
        persist();
    }

    public void reload() {
        properties.clear();
        if (!Files.exists(path)) return;
        try (InputStream in = Files.newInputStream(path)) {
            properties.load(in);
        } catch (IOException ignored) {
        }
    }

    private void persist() {
        try {
            Path parent = path.getParent();
            if (parent != null) Files.createDirectories(parent);
            try (OutputStream out = Files.newOutputStream(path)) {
                properties.store(out, "ExampleMod settings");
            }
        } catch (IOException ignored) {
        }
    }

    private static String keyFor(String moduleName) {
        return "module." + moduleName;
    }
}
