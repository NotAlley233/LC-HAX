package com.example.mod.module;

public interface Module {
    String name();

    String description();

    Category category();

    boolean enabled();

    void setEnabled(boolean enabled);
}
