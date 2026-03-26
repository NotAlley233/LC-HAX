package com.example.mod.module;

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
        if (enabled) {
            EventBus.subscribe(this);
            onEnable();
        } else {
            EventBus.unsubscribe(this);
            onDisable();
        }
    }

    protected void onEnable() {}

    protected void onDisable() {}
}
