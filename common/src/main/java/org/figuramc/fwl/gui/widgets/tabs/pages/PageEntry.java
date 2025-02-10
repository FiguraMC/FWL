package org.figuramc.fwl.gui.widgets.tabs.pages;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.Resizeable;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.jetbrains.annotations.Nullable;

import static org.figuramc.fwl.utils.RenderUtils.fill;

public interface PageEntry {
    default boolean renderIcon(GuiGraphics graphics, float x, float y, float width, float height) {
        return false;
    }

    default void onClose() {}

    FWLWidget getPage(float width, float height);

    AbstractComponent getTitle();
    default @Nullable AbstractComponent getTooltip() {
        return null;
    }
}
