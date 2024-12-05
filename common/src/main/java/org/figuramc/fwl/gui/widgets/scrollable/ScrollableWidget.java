package org.figuramc.fwl.gui.widgets.scrollable;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.figuramc.fwl.gui.widgets.FWLContainerWidget;

public abstract class ScrollableWidget implements FWLContainerWidget {
    public abstract float offsetX();
    public abstract float offsetY();

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(-offsetX(), -offsetY(), 0);
        FWLContainerWidget.super.render(graphics, mouseX + offsetX(), mouseY + offsetY(), delta);
        stack.popPose();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        FWLContainerWidget.super.mouseMoved(mouseX + offsetX(), mouseY + offsetY());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        return FWLContainerWidget.super.mouseClicked(mouseX + offsetX(), mouseY + offsetY(), button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        return FWLContainerWidget.super.mouseReleased(mouseX + offsetX(), mouseY + offsetY(), button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        return FWLContainerWidget.super.mouseScrolled(mouseX + offsetX(), mouseY + offsetY(), amount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return FWLContainerWidget.super.mouseDragged(mouseX + offsetX(), mouseY + offsetY(), button, deltaX, deltaY);
    }
}
