package com.example.mod.property.properties;

import com.example.mod.property.Property;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BooleanProperty extends Property<Boolean> {
    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;

    public BooleanProperty(String name, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        super(name);
        this.getter = getter;
        this.setter = setter;
    }

    @Override public Boolean getValue() { return getter.get(); }
    @Override public void setValue(Boolean value) { setter.accept(value != null && value); }

    @Override
    public boolean parseString(String newValue) {
        boolean value = Boolean.TRUE.equals(getter.get());
        if (newValue == null) {
            setter.accept(!value);
            return true;
        }
        if (equalsAnyIgnoreCase(newValue, "true", "on", "1")) {
            boolean changed = !value;
            setter.accept(true);
            return changed;
        }
        if (equalsAnyIgnoreCase(newValue, "false", "off", "0")) {
            boolean changed = value;
            setter.accept(false);
            return changed;
        }
        return false;
    }

    @Override public String formatValue() { return Boolean.TRUE.equals(getter.get()) ? "true" : "false"; }
    @Override public String getValuePrompt() { return "true/false"; }

    private static boolean equalsAnyIgnoreCase(String s, String... options) {
        for (String o : options) if (s.equalsIgnoreCase(o)) return true;
        return false;
    }
}

