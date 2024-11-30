package org.figuramc.fwl.gui.widgets.descriptors;

import org.jetbrains.annotations.Nullable;

public class SliderDescriptor {
    private float x, y, width, height;
    private float progress;
    private Orientation orientation;
    private WidgetState state;
    private int stateTicks;

    public SliderDescriptor(float x, float y, float width, float height, float progress) {
        this(x,y,width,height,progress, null, null, 0);
    }

    public SliderDescriptor(float x, float y, float width, float height, float progress, @Nullable Orientation orientation, @Nullable WidgetState state, int stateTicks) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.progress = progress;
        this.orientation = orientation != null ? orientation : Orientation.VERTICAL;
        this.state = state != null ? state : WidgetState.DEFAULT;
        this.stateTicks = stateTicks;
    }

    public float x() {
        return x;
    }

    public SliderDescriptor setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public SliderDescriptor setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public SliderDescriptor setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public SliderDescriptor setHeight(float height) {
        this.height = height;
        return this;
    }

    public float progress() {
        return progress;
    }

    public SliderDescriptor setProgress(float progress) {
        this.progress = progress;
        return this;
    }

    public Orientation orientation() {
        return orientation;
    }

    public SliderDescriptor setOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public WidgetState state() {
        return state;
    }

    public SliderDescriptor setState(WidgetState state) {
        this.state = state;
        return this;
    }

    public int stateTicks() {
        return stateTicks;
    }

    public SliderDescriptor setStateTicks(int stateTicks) {
        this.stateTicks = stateTicks;
        return this;
    }
}
