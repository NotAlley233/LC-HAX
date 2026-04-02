package com.example.mod.module;

import com.example.mod.core.ToggleAnnouncements;
import com.example.mod.module.modules.render.DynamicIsland;
import com.example.mod.util.ChatUtil;
import net.weavemc.api.event.EventBus;

public abstract class BaseModule implements Module {
    private boolean enabled;
    private final String name;
    private final String description;
    private final Category category;

    protected BaseModule(String name, String description, Category category, boolean defaultEnabled) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = defaultEnabled;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Category category() {
        return category;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;

        // Push module toggle notifications into DynamicIsland.
        // Skip DynamicIsland itself to avoid self-trigger loops.
        if (!"dynamicisland".equalsIgnoreCase(this.name)) {
            try {
                DynamicIsland.onModuleToggle(this.name, enabled);
            } catch (Throwable ignored) {
                // Keep module toggling robust even if DynamicIsland isn't initialized.
            }
        }

        if (enabled) {
            EventBus.subscribe(this);
            onEnable();
            if (ToggleAnnouncements.enabled()) {
                ChatUtil.info(name + " (§aON§r)");
            }
        } else {
            if (ToggleAnnouncements.enabled()) {
                ChatUtil.info(name + " (§cOFF§r)");
            }
            EventBus.unsubscribe(this);
            onDisable();
        }
    }

    protected void onEnable() {}

    protected void onDisable() {}
}
