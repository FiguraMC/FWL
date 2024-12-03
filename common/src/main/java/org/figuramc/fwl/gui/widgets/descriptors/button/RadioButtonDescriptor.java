package org.figuramc.fwl.gui.widgets.descriptors.button;

import org.figuramc.fwl.utils.BiTickCounter;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class RadioButtonDescriptor extends ClickableDescriptor {
    private final BiTickCounter active;

    public RadioButtonDescriptor(float x, float y, float width, float height, boolean active) {
        super(x, y, width, height);
        this.active = new BiTickCounter(active ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        this.active.setInc(active);
    }

    public RadioButtonDescriptor setActive(boolean active) {
        if (this.active.inc() != active) {
            this.active.reset();
            this.active.setInc(active);
        }
        return this;
    }

    public boolean active() {
        return active.inc();
    }

    public int activeTicks() {
        return active.get();
    }

    public void tick() {
        super.tick();
        active.tick();
    }
}
