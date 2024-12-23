package org.figuramc.fwl.gui.themes;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.descriptors.*;
import org.figuramc.fwl.gui.widgets.descriptors.input.TextInputDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.ContextMenuDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.IconDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.SliderDescriptor;
import org.jetbrains.annotations.Nullable;
import org.figuramc.fwl.gui.widgets.descriptors.button.CheckboxDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.RadioButtonDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonDescriptor;

public abstract class FWLTheme {
    // WIDGETS RENDERING
    public abstract void renderButton(GuiGraphics graphics, float delta, ButtonDescriptor button);
    public abstract void renderCheckbox(GuiGraphics graphics, float delta, CheckboxDescriptor checkbox);
    public abstract void renderRadioButton(GuiGraphics graphics, float delta, RadioButtonDescriptor button);
    public abstract void renderScrollBar(GuiGraphics graphics, float delta, ScrollBarDescriptor scrollBar);
    public abstract void renderSlider(GuiGraphics graphics, float delta, SliderDescriptor slider);
    public abstract void renderTextInput(GuiGraphics graphics, float delta, TextInputDescriptor textInput);
    public abstract void renderContextMenu(GuiGraphics graphics, float delta, ContextMenuDescriptor menu);
    public abstract void renderBounds(GuiGraphics graphics, float delta, BoundsDescriptor descriptor);
    public abstract void fillBounds(GuiGraphics graphics, float delta, BoundsDescriptor descriptor);
    public abstract void renderIcon(GuiGraphics graphics, IconDescriptor icon);

    // CONFIGURATION
    public abstract void applyPreset(@Nullable JsonObject preset);
    public abstract JsonObject savePreset();
    public abstract @Nullable FWLWidget settingsRootWidget(float areaWidth, float areaHeight);

    public abstract Integer getColor(String... path);
    public Integer getColor(ResourceLocation path) {
        return getColor(path.getPath());
    }

    public int getColorOrDefault(ResourceLocation type, int fallback) {
        return getColorOrDefault(fallback, type.getPath());
    }

    public int getColorOrDefault(int fallback, String... path) {
        Integer color = getColor(path);
        return color != null ? color : fallback;
    }

    public interface DescriptorRenderer<T extends FWLDescriptor> {
        void render(GuiGraphics graphics, float delta, T desc);
    }

}
