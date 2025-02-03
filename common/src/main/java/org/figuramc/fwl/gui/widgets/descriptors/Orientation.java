package org.figuramc.fwl.gui.widgets.descriptors;

public enum Orientation {
    HORIZONTAL,
    VERTICAL;

    public boolean allowScroll(boolean shiftPressed) {
        return (this == HORIZONTAL) == shiftPressed;
    }

    public <T> T switchBy(T horizontal, T vertical) {
        return this == HORIZONTAL ? horizontal : vertical;
    }
}
