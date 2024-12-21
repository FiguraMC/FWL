package org.figuramc.fwl.gui.widgets.descriptors.misc;

import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.descriptors.PathStyle;
import org.jetbrains.annotations.Nullable;

public class SplitterDescriptor {
    private float x, y, thickness, length;
    private Orientation orientation;
    private PathStyle style;

    public SplitterDescriptor(float x, float y, float length) {
        this(x, y, 0, length, null, null);
    }

    public SplitterDescriptor(float x, float y, float length, @Nullable Orientation orientation) {
        this(x, y, 0, length, orientation, null);
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
