package org.figuramc.fwl.gui.widgets.descriptors.button;

import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.figuramc.fwl.utils.BiTickCounter;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class CheckboxDescriptor extends ClickableDescriptor {
    public static final String WIDGET_TYPE = "checkbox";

    private float x, y, width, height;
    private @Nullable Vector2f clickPos;
    private final BiTickCounter checked;

    public CheckboxDescriptor(float x, float y, float width, float height, boolean checked) {
        super(x,y,width,height);
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

    @Override
    public String widgetType() {
        return WIDGET_TYPE;
    }
}
