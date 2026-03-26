package com.example.mod.module.modules;

import com.example.mod.module.BaseModule;

public class ModuleA extends BaseModule {
    public ModuleA() {
        super(false);
    }

    @Override public String name() { return "a"; }
    @Override public String description() { return "Example module A (template)."; }
}
