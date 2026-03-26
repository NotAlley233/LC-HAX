package com.example.mod.property.properties;

import com.example.mod.property.Property;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatProperty extends Property<Float> {
    private final Supplier<Float> getter;
    private final Consumer<Float> setter;
    private final String prompt;

    public FloatProperty(String name, Supplier<Float> getter, Consumer<Float> setter) {
        this(name, getter, setter, "float");
    }

    public FloatProperty(String name, Supplier<Float> getter, Consumer<Float> setter, String prompt) {
        super(name);
        this.getter = getter;
        this.setter = setter;
        this.prompt = prompt;
    }

    @Override public Float getValue() { return getter.get(); }
    @Override public void setValue(Float value) { setter.accept(value); }

    @Override
    public boolean parseString(String newValue) {
        if (newValue == null) return false;
        try {
            setter.accept(Float.parseFloat(newValue.trim()));
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    @Override public String formatValue() { return String.valueOf(getter.get()); }
    @Override public String getValuePrompt() { return prompt; }
}

