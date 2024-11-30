package org.figuramc.fwl.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;

public class VanillaRenderer implements Renderable {
    private final net.minecraft.client.gui.components.Renderable parent;

    public VanillaRenderer(net.minecraft.client.gui.components.Renderable parent) {
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        parent.render(graphics, (int) mouseX, (int) mouseY, delta);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        parent.render(graphics, mouseX, mouseY, delta);
    }
}
