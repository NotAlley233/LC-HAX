package com.example.mod.module;

import java.util.*;

public class ModuleManager {
    private final Map<String, Module> byName = new HashMap<>();
    private final List<Module> modules = new ArrayList<>();

    public void register(Module module) {
        modules.add(module);
        byName.put(module.name().toLowerCase(Locale.ROOT), module);
    }

    public Module get(String nameLowercase) {
        if (nameLowercase == null) return null;
        return byName.get(nameLowercase.toLowerCase(Locale.ROOT));
    }

    public List<Module> all() {
        return Collections.unmodifiableList(modules);
    }
}
