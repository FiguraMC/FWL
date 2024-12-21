package org.figuramc.fwl.gui.widgets.tabs;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.widgets.FWLWidget;

public interface TabPage extends FWLWidget {
    void pageResize(float width, float height);
    void renderIcon(GuiGraphics graphics, float x, float y, float width, float height);
    Component getTitle();
}
