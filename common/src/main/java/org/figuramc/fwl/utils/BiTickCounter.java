package org.figuramc.fwl.utils;

public class BiTickCounter {
    private int val;
    private boolean incrementing;

    public BiTickCounter() {}

    public BiTickCounter(int initialValue) {
        val = initialValue;
    }

    public void setInc(boolean incrementing) {
        this.incrementing = incrementing;
        if (!incrementing) val = 0;
    }

    public boolean inc() {
        return incrementing;
    }

    public void tick() {
        if (incrementing && val < Integer.MAX_VALUE) val++;
        else if (val > Integer.MIN_VALUE) val--;
    }

    public int get() {
        return val;
    }

    public void set(int val) {
        this.val = val;
    }

    public void reset() {
        val = 0;
    }
}
