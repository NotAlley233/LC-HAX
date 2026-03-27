package com.example.mod.property.properties;

import com.example.mod.property.Property;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class IntProperty extends Property<Integer> {
    private final IntSupplier getter;
    private final IntConsumer setter;
    private final String prompt;
    private final int min;
    private final int max;

    public IntProperty(String name, IntSupplier getter, IntConsumer setter, int min, int max) {
        this(name, getter, setter, min, max, "int");
    }

    public IntProperty(String name, IntSupplier getter, IntConsumer setter, int min, int max, String prompt) {
        super(name);
        this.getter = getter;
        this.setter = setter;
        this.min = min;
        this.max = max;
        this.prompt = prompt;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override public Integer getValue() { return getter.getAsInt(); }
    @Override public void setValue(Integer value) { 
        if (value != null) {
            setter.accept(Math.max(min, Math.min(max, value)));
        }
    }

    @Override
    public boolean parseString(String newValue) {
        if (newValue == null) return false;
        try {
            setter.accept(Integer.parseInt(newValue.trim()));
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    @Override public String formatValue() { return Integer.toString(getter.getAsInt()); }
    @Override public String getValuePrompt() { return prompt; }
}

