package org.figuramc.fwl.gui.widgets.descriptors.input;

import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.figuramc.fwl.utils.BiTickCounter;

public class TextInputDescriptor extends ClickableDescriptor {
    public static final String WIDGET_TYPE = "text_input";

    private final BiTickCounter immutable = new BiTickCounter(Integer.MIN_VALUE);

    public TextInputDescriptor(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public TextInputDescriptor setImmutable(boolean immutable) {
        if (this.immutable.inc() != immutable) {
            this.immutable.reset();
            this.immutable.setInc(immutable);
        }
        return this;
    }

    public boolean immutable() {
        return immutable.inc();
    }

    public int immutableTicks() {
        return immutable.get();
    }

    @Override
    public void tick() {
        super.tick();
        immutable.tick();
    }

    @Override
    public String widgetType() {
        return WIDGET_TYPE;
    }
}
