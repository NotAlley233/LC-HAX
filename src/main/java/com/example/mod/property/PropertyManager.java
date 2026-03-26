package com.example.mod.property;

import com.example.mod.module.Module;

import java.util.*;

public class PropertyManager {
    private final Map<Class<?>, List<Property<?>>> properties = new HashMap<>();

    public void register(Module module, Property<?>... props) {
        if (module == null || props == null) return;
        List<Property<?>> list = properties.computeIfAbsent(module.getClass(), k -> new ArrayList<>());
        list.addAll(Arrays.asList(props));
    }

    public List<Property<?>> getProperties(Module module) {
        if (module == null) return null;
        return properties.get(module.getClass());
    }

    public Property<?> getProperty(Module module, String name) {
        if (module == null || name == null) return null;
        List<Property<?>> list = properties.get(module.getClass());
        if (list == null) return null;
        for (Property<?> p : list) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }
}

