package org.lexize.lwl.widgets.descriptors;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class CheckboxDescriptor {
    private int stateTicks;
    private WidgetState state;

    private float x, y, width, height;
    private @Nullable Vector2f clickPos;
    private boolean checked;

    public CheckboxDescriptor(float x, float y, float width, float height, boolean checked) {
        this(x,y,width,height, checked, null, null, 0);
    }

    public CheckboxDescriptor(float x, float y, float width, float height, boolean checked, @Nullable Vector2f clickPos, @Nullable WidgetState widgetState, int stateTicks) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.checked = checked;
        this.clickPos = clickPos;
        this.state = widgetState != null ? widgetState : WidgetState.DEFAULT;
        this.stateTicks = stateTicks;
    }

    public int stateTicks() {
        return stateTicks;
    }

    public CheckboxDescriptor setStateTicks(int stateTicks) {
        this.stateTicks = stateTicks;
        return this;
    }

    public WidgetState state() {
        return state;
    }

    public CheckboxDescriptor setState(WidgetState state) {
        this.state = state;
        return this;
    }

    public float x() {
        return x;
    }

    public CheckboxDescriptor setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public CheckboxDescriptor setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public CheckboxDescriptor setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public CheckboxDescriptor setHeight(float height) {
        this.height = height;
        return this;
    }

    public @Nullable Vector2f clickPos() {
        return clickPos;
    }

    public CheckboxDescriptor setClickPos(@Nullable Vector2f clickPos) {
        this.clickPos = clickPos;
        return this;
    }

    public boolean checked() {
        return checked;
    }

    public CheckboxDescriptor setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }
}
