package org.lexize.lwl.utils;

public class ToggleTickCounter {
    private int val;
    private boolean enabled;

    public ToggleTickCounter setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) val = 0;
        return this;
    }

    public boolean enabled() {
        return enabled;
    }

    public void increment() {
        if (enabled) val++;
    }

    public int get() {
        return val;
    }

    public void reset() {
        val = 0;
    }
}
