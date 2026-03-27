package com.example.mod.property.properties;

import com.example.mod.property.Property;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModeProperty extends Property<String> {
    private final Supplier<String> getter;
    private final Consumer<String> setter;
    private final List<String> modes;

    public ModeProperty(String name, Supplier<String> getter, Consumer<String> setter, String... modes) {
        super(name);
        this.getter = getter;
        this.setter = setter;
        this.modes = Arrays.asList(modes);
    }

    @Override public String getValue() { return getter.get(); }
    @Override public void setValue(String value) { setter.accept(value); }

    @Override
    public boolean parseString(String newValue) {
        if (newValue == null) return false;
        for (String m : modes) {
            if (m.equalsIgnoreCase(newValue)) {
                setter.accept(m);
                return true;
            }
        }
        return false;
    }

    @Override public String formatValue() { return String.valueOf(getter.get()); }

    @Override
    public String getValuePrompt() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < modes.size(); i++) {
            if (i > 0) sb.append("/");
            sb.append(modes.get(i).toLowerCase(Locale.ROOT));
        }
        return sb.toString();
    }

    public List<String> getModes() {
        return modes;
    }
}
