package org.lexize.lwl.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;

public interface Renderable extends net.minecraft.client.gui.components.Renderable {
    void render(GuiGraphics graphics, float mouseX, float mouseY, float delta);

    @Override
    default void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        render(graphics, (float) mouseX, (float) mouseY, delta);
    }
}
