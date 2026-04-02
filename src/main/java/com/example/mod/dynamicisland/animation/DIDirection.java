package com.example.mod.dynamicisland.animation;

/**
 * Animation direction enum.
 * Ported from: https://github.com/Jonqwq/DynamicIsland-System-for-mc-hacked-client
 * (MIT license, attribution retained)
 */
public enum DIDirection {
    FORWARDS,
    BACKWARDS;

    public DIDirection opposite() {
        if (this == FORWARDS) {
            return BACKWARDS;
        }
        return FORWARDS;
    }

    public boolean forwards() {
        return this == FORWARDS;
    }

    public boolean backwards() {
        return this == BACKWARDS;
    }
}
