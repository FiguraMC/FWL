package org.lexize.lwl.gui.widgets.descriptors.button;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.lexize.lwl.utils.BiTickCounter;

public class CheckboxDescriptor extends ClickableDescriptor {
    private float x, y, width, height;
    private @Nullable Vector2f clickPos;
    private final BiTickCounter checked;

    public CheckboxDescriptor(float x, float y, float width, float height, boolean checked) {
        super(x,y,width,height);
        this.checked = new BiTickCounter(checked ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        this.checked.setInc(checked);
    }

    public CheckboxDescriptor(float x, float y, float width, float height, boolean checked, @Nullable Vector2f clickPos) {
        super(x,y,width,height,clickPos);
        this.checked = new BiTickCounter(checked ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        this.checked.setInc(checked);
    }


    public CheckboxDescriptor setChecked(boolean checked) {
        if (this.checked.inc() != checked) {
            this.checked.reset();
            this.checked.setInc(checked);
        }
        return this;
    }

    public boolean checked() {
        return checked.inc();
    }

    public int checkedTicks() {
        return checked.get();
    }

    public void tick() {
        super.tick();
        checked.tick();
    }
}
