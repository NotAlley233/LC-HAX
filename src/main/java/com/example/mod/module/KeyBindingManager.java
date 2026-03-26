package com.example.mod.module;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyBindingManager {
    // moduleName (lowercase) -> keyCode
    private final Map<String, Integer> moduleToKey = new ConcurrentHashMap<>();
    // keyCode -> moduleName (lowercase)
    private final Map<Integer, String> keyToModule = new ConcurrentHashMap<>();

    /**
     * Binds a module to a key code.
     * @param moduleName The module name
     * @param keyCode The key code
     * @param force If true, overrides existing binding for the key if it exists.
     * @return true if successful, false if there is a conflict and force is false.
     */
    public boolean bind(String moduleName, int keyCode, boolean force) {
        String lowerName = moduleName.toLowerCase();
        
        // Check for conflicts on the key
        if (!force && keyToModule.containsKey(keyCode)) {
            String existingModule = keyToModule.get(keyCode);
            if (!existingModule.equals(lowerName)) {
                return false; // Conflict detected
            }
        }

        // Unbind existing key for this module if any
        unbind(lowerName);

        // If another module was using this key and force=true, unbind it from that module
        if (keyToModule.containsKey(keyCode)) {
            String existingModule = keyToModule.get(keyCode);
            moduleToKey.remove(existingModule);
        }

        moduleToKey.put(lowerName, keyCode);
        keyToModule.put(keyCode, lowerName);
        return true;
    }

    /**
     * Unbinds a module.
     * @param moduleName The module name
     * @return true if it was bound, false otherwise.
     */
    public boolean unbind(String moduleName) {
        String lowerName = moduleName.toLowerCase();
        Integer existingKey = moduleToKey.remove(lowerName);
        if (existingKey != null) {
            keyToModule.remove(existingKey);
            return true;
        }
        return false;
    }

    public Integer getBoundKey(String moduleName) {
        return moduleToKey.get(moduleName.toLowerCase());
    }

    public String getBoundModule(int keyCode) {
        return keyToModule.get(keyCode);
    }

    public Map<String, Integer> getAllBindings() {
        return Collections.unmodifiableMap(moduleToKey);
    }

    public void clear() {
        moduleToKey.clear();
        keyToModule.clear();
    }
    
    public void loadFromMap(Map<String, Integer> bindings) {
        clear();
        for (Map.Entry<String, Integer> entry : bindings.entrySet()) {
            bind(entry.getKey(), entry.getValue(), true);
        }
    }
}
