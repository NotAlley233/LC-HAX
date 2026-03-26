package com.example.mod.module;

public abstract class BaseModule implements Module {
    private boolean enabled;

    protected BaseModule(boolean defaultEnabled) {
        this.enabled = defaultEnabled;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    protected void onEnable() {}

    protected void onDisable() {}
}
