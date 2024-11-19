package org.lexize.lwl.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import org.lexize.lwl.LWL;
import org.lexize.lwl.themes.LWLBreeze;
import org.lexize.lwl.widgets.descriptors.ButtonDescriptor;
import org.lexize.lwl.widgets.descriptors.WidgetState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class GuiMixin {
    @Unique
    private LWLBreeze breeze = new LWLBreeze();
    @Unique
    private ButtonDescriptor desc = new ButtonDescriptor(10, 10, 150, 25);
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        desc.setState(desc.mouseIn(mouseX, mouseY) ? WidgetState.FOCUSED : WidgetState.DEFAULT);
        LWL.pushTheme(breeze);
        breeze.renderButton(graphics, delta, desc);
        LWL.popTheme();
    }
}
