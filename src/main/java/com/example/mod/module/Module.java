package com.example.mod.module;

public interface Module {
    String name();

    String description();

    boolean enabled();

    void setEnabled(boolean enabled);
}
