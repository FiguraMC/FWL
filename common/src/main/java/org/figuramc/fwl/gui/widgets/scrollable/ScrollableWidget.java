package org.figuramc.fwl.gui.widgets.scrollable;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.figuramc.fwl.gui.widgets.containers.AbstractFWLContainerWidget;
import org.joml.Vector2d;

public abstract class ScrollableWidget extends AbstractFWLContainerWidget {
    public abstract float offsetX();
    public abstract float offsetY();

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(-offsetX(), -offsetY(), 0);
        super.render(graphics, mouseX + offsetX(), mouseY + offsetY(), delta);
        stack.popPose();
    }

    @Override
    public Vector2d getInteractionMousePos(double mouseX, double mouseY) {
        return new Vector2d(mouseX + offsetX(), mouseY + offsetY());
    }
}
