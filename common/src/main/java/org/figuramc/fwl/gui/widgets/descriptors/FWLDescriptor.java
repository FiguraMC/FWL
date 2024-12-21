package org.figuramc.fwl.gui.widgets.descriptors;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.figuramc.fwl.utils.Rectangle;

public interface FWLDescriptor {
    default boolean mouseIn(float x, float y) {
        return boundaries().pointIn(x, y);
    }

    default ScreenRectangle screenRectangle() {
        Rectangle bounds = boundaries();
        return new ScreenRectangle((int) bounds.x(), (int) bounds.y(), (int) bounds.width(), (int) bounds.height());
    }
    Rectangle boundaries();
    String widgetType();
}
