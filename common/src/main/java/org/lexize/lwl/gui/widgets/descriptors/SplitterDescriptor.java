package org.lexize.lwl.gui.widgets.descriptors;

import org.jetbrains.annotations.Nullable;
import org.lexize.lwl.LWL;

public class SplitterDescriptor {
    private float x, y, thickness, length;
    private Orientation orientation;
    private PathStyle style;

    public SplitterDescriptor(float x, float y, float length) {
        this(x, y, LWL.peekTheme().defaultBorderRadius(), length, null, null);
    }

    public SplitterDescriptor(float x, float y, float length, @Nullable Orientation orientation) {
        this(x, y, LWL.peekTheme().defaultBorderRadius(), length, orientation, null);
    }

    public SplitterDescriptor(float x, float y, float thickness, float length) {
        this(x, y, thickness, length, null, null);
    }

    public SplitterDescriptor(float x, float y, float thickness, float length, @Nullable Orientation orientation, @Nullable PathStyle style) {
        this.x = x;
        this.y = y;
        this.thickness = thickness;
        this.length = length;
        this.orientation = orientation != null ? orientation : Orientation.VERTICAL;
        this.style = style != null ? style : PathStyle.SOLID;
    }

    public float x() {
        return x;
    }

    public SplitterDescriptor setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public SplitterDescriptor setY(float y) {
        this.y = y;
        return this;
    }

    public float thickness() {
        return thickness;
    }

    public SplitterDescriptor setThickness(float thickness) {
        this.thickness = thickness;
        return this;
    }

    public float length() {
        return length;
    }

    public SplitterDescriptor setLength(float length) {
        this.length = length;
        return this;
    }

    public Orientation orientation() {
        return orientation;
    }

    public SplitterDescriptor setOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }
}
