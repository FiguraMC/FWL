package org.figuramc.fwl.gui.widgets.descriptors.button;

import org.figuramc.fwl.gui.widgets.descriptors.FWLDescriptor;
import org.figuramc.fwl.utils.BiTickCounter;
import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public abstract class ClickableDescriptor implements FWLDescriptor {
    private float x, y, width, height;
    private final Vector2f clickPos = new Vector2f();
    private final Vector2f hoverPos = new Vector2f();
    private final BiTickCounter hovered = new BiTickCounter(Integer.MIN_VALUE);
    private final BiTickCounter focused = new BiTickCounter(Integer.MIN_VALUE);
    private final BiTickCounter clicked = new BiTickCounter(Integer.MIN_VALUE);
    private final BiTickCounter disabled = new BiTickCounter(Integer.MIN_VALUE);

    public ClickableDescriptor(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ClickableDescriptor setFocused(boolean focused) {
        if (this.focused.inc() != focused) {
            this.focused.reset();
            this.focused.setInc(focused);
        }
        return this;
    }

    public ClickableDescriptor setHovered(boolean hovered) {
        if (this.hovered.inc() != hovered) {
            this.hovered.reset();
            this.hovered.setInc(hovered);
        }
        return this;
    }

    public ClickableDescriptor setClicked(boolean clicked) {
        if (this.clicked.inc() != clicked) {
            this.clicked.reset();
            this.clicked.setInc(clicked);
        }
        return this;
    }

    public ClickableDescriptor setDisabled(boolean disabled) {
        if (this.disabled.inc() != disabled) {
            this.disabled.reset();
            this.disabled.setInc(disabled);
        }
        return this;
    }

    public boolean focused() {
        return focused.inc();
    }

    public boolean hovered() {
        return hovered.inc();
    }

    public boolean clicked() {
        return clicked.inc();
    }

    public boolean disabled() {
        return disabled.inc();
    }

    public int focusedTicks() {
        return focused.get();
    }

    public int hoveredTicks() {
        return hovered.get();
    }

    public int clickedTicks() {
        return clicked.get();
    }

    public int disabledTicks() {
        return disabled.get();
    }

    public float x() {
        return x;
    }

    public ClickableDescriptor setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public ClickableDescriptor setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public ClickableDescriptor setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public ClickableDescriptor setHeight(float height) {
        this.height = height;
        return this;
    }

    public @Nullable Vector2f clickPos() {
        return clickPos;
    }

    public void setClickPos(float x, float y) {
        this.clickPos.set(x, y);
    }

    public void setHoverPos(float x, float y) {
        this.hoverPos.set(x, y);
    }

    public void tick() {
        focused.tick();
        clicked.tick();
        hovered.tick();
        disabled.tick();
    }

    public boolean mouseIn(float x, float y) {
        return x >= this.x && x <= this.x + width &&
                y >= this.y && y <= this.y + height;
    }

    @Override
    public Rectangle boundaries() {
        return new Rectangle(x, y, width, height);
    }
}
