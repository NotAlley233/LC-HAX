package com.example.mod.property.properties;

import com.example.mod.property.Property;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextProperty extends Property<String> {
    private final Supplier<String> getter;
    private final Consumer<String> setter;
    private final String prompt;

    public TextProperty(String name, Supplier<String> getter, Consumer<String> setter) {
        this(name, getter, setter, "text");
    }

    public TextProperty(String name, Supplier<String> getter, Consumer<String> setter, String prompt) {
        super(name);
        this.getter = getter;
        this.setter = setter;
        this.prompt = prompt;
    }

    @Override public String getValue() { return getter.get(); }
    @Override public void setValue(String value) { setter.accept(value); }

    @Override
    public boolean parseString(String newValue) {
        if (newValue == null) return false;
        setter.accept(newValue);
        return true;
    }

    @Override public String formatValue() { return String.valueOf(getter.get()); }
    @Override public String getValuePrompt() { return prompt; }
}

