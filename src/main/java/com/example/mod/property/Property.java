package com.example.mod.property;

public abstract class Property<T> {
    private final String name;
    private boolean visible = true;

    protected Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return visible;
    }

    public Property<T> setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public abstract T getValue();

    public abstract void setValue(T value);

    /**
     * @param newValue string to parse; may be null for Boolean-style toggles
     * @return true if value changed (or action succeeded)
     */
    public abstract boolean parseString(String newValue);

    public abstract String formatValue();

    public abstract String getValuePrompt();
}

