package com.example.mod.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    UTILITY("Utility"),
    ADVANCED("Advanced"),
    BEDWARS("Bedwars");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}