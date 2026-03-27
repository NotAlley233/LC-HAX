package com.example.mod.property.properties;

import com.example.mod.property.Property;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatProperty extends Property<Float> {
    private final Supplier<Float> getter;
    private final Consumer<Float> setter;
    private final String prompt;
    private final float min;
    private final float max;

    public FloatProperty(String name, Supplier<Float> getter, Consumer<Float> setter) {
        this(name, getter, setter, -100f, 100f, "float");
    }

    public FloatProperty(String name, Supplier<Float> getter, Consumer<Float> setter, String prompt) {
        this(name, getter, setter, -100f, 100f, prompt);
    }

    public FloatProperty(String name, Supplier<Float> getter, Consumer<Float> setter, float min, float max) {
        this(name, getter, setter, min, max, "float");
    }

    public FloatProperty(String name, Supplier<Float> getter, Consumer<Float> setter, float min, float max, String prompt) {
        super(name);
        this.getter = getter;
        this.setter = setter;
        this.min = min;
        this.max = max;
        this.prompt = prompt;
    }

    @Override public Float getValue() { return getter.get(); }
    @Override public void setValue(Float value) { setter.accept(Math.max(min, Math.min(max, value))); }
    public float getMin() { return min; }
    public float getMax() { return max; }

    @Override
    public boolean parseString(String newValue) {
        if (newValue == null) return false;
        try {
            float value = Float.parseFloat(newValue.trim());
            setter.accept(Math.max(min, Math.min(max, value)));
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    @Override public String formatValue() { return String.valueOf(getter.get()); }
    @Override public String getValuePrompt() { return prompt; }
}
