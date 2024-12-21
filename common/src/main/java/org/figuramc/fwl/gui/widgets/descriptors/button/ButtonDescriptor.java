package org.figuramc.fwl.gui.widgets.descriptors.button;

import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.jetbrains.annotations.Nullable;

public class ButtonDescriptor extends ClickableDescriptor {
    public static final String WIDGET_TYPE = "button";

    private ResourceLocation type;

    public ButtonDescriptor(float x, float y, float width, float height) {
        this(x, y, width, height, null);
    }

    public ButtonDescriptor(float x, float y, float width, float height, @Nullable ResourceLocation type) {
        super(x, y, width, height);
        this.type = type != null ? type : ButtonTypes.DEFAULT;
    }

    public ResourceLocation type() {
        return type;
    }

    public ButtonDescriptor setType(ResourceLocation type) {
        this.type = type;
        return this;
    }

    @Override
    public String widgetType() {
        return WIDGET_TYPE;
    }
}
