package com.example.mod.module;

import java.util.*;

public class ModuleManager {
    private final Map<String, Module> byName = new HashMap<>();
    private final List<Module> modules = new ArrayList<>();

    public void register(Module module) {
        if (module == null || module.name() == null) return;
        String key = module.name().toLowerCase(Locale.ROOT);
        if (byName.containsKey(key)) return;
        modules.add(module);
        byName.put(key, module);
    }

    public Module get(String nameLowercase) {
        if (nameLowercase == null) return null;
        return byName.get(nameLowercase.toLowerCase(Locale.ROOT));
    }

    public List<Module> all() {
        return Collections.unmodifiableList(modules);
    }
}
