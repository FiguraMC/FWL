package org.lexize.lwl.gui.widgets.descriptors;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.lexize.lwl.utils.BiTickCounter;

public class CheckboxDescriptor implements WidgetDescriptor {
    private float x, y, width, height;
    private @Nullable Vector2f clickPos;
    private final BiTickCounter hovered = new BiTickCounter();
    private final BiTickCounter focused = new BiTickCounter();
    private final BiTickCounter clicked = new BiTickCounter();
    private final BiTickCounter disabled = new BiTickCounter();
    private final BiTickCounter checked = new BiTickCounter();

    public CheckboxDescriptor(float x, float y, float width, float height, boolean checked) {
        this(x,y,width,height, checked, null);
    }

    public CheckboxDescriptor(float x, float y, float width, float height, boolean checked, @Nullable Vector2f clickPos) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.clickPos = clickPos;
        this.checked.setInc(checked);
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

    public CheckboxDescriptor setFocused(boolean focused) {
        if (this.focused.inc() != focused) {
            this.focused.reset();
            this.focused.setInc(focused);
        }
        return this;
    }

    public CheckboxDescriptor setHovered(boolean hovered) {
        if (this.hovered.inc() != hovered) {
            this.hovered.reset();
            this.hovered.setInc(hovered);
        }
        return this;
    }

    public CheckboxDescriptor setClicked(boolean clicked) {
        if (this.clicked.inc() != clicked) {
            this.clicked.reset();
            this.clicked.setInc(clicked);
        }
        return this;
    }

    public CheckboxDescriptor setDisabled(boolean disabled) {
        if (this.disabled.inc() != disabled) {
            this.disabled.reset();
            this.disabled.setInc(disabled);
        }
        return this;
    }

    public CheckboxDescriptor setChecked(boolean checked) {
        if (this.checked.inc() != checked) {
            this.checked.reset();
            this.checked.setInc(checked);
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

    public boolean checked() {
        return checked.inc();
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

    public int checkedTicks() {
        return disabled.get();
    }

    public void tick() {
        focused.tick();
        clicked.tick();
        hovered.tick();
        disabled.tick();
        checked.tick();
    }

    @Override
    public boolean mouseIn(float x, float y) {
        return x >= this.x && x <= this.x + width &&
                y >= this.y && y <= this.y + height;
    }

    @Override
    public ScreenRectangle getRectangle() {
        return new ScreenRectangle((int) x, (int) y, (int) width, (int) height);
    }
}
