package org.figuramc.fwl.gui.widgets.tabs.pages;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.Resizeable;
import org.jetbrains.annotations.Nullable;

import static org.figuramc.fwl.utils.RenderUtils.fill;

public interface PageEntry {
    default boolean renderIcon(GuiGraphics graphics, float x, float y, float width, float height) {
        return false;
    }

    FWLWidget getPage(float width, float height);

    Component getTitle();
    @Nullable Component getTooltip();
}
