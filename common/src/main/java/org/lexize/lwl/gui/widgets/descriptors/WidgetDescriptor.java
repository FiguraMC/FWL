package org.lexize.lwl.gui.widgets.descriptors;

import net.minecraft.client.gui.navigation.ScreenRectangle;

public interface WidgetDescriptor {
    boolean mouseIn(float x, float y);
    ScreenRectangle getRectangle();
}
