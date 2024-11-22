package org.lexize.lwl.gui.widgets.descriptors.button;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class ButtonDescriptor extends ClickableDescriptor {
    private ResourceLocation type;

    public ButtonDescriptor(float x, float y, float width, float height) {
        this(x, y, width, height, null, null);
    }

    public ButtonDescriptor(float x, float y, float width, float height, @Nullable Vector2f clickPos, @Nullable ResourceLocation type) {
        super(x, y, width, height, clickPos);
        this.type = type != null ? type : ButtonTypes.DEFAULT;
    }

    public ResourceLocation type() {
        return type;
    }

    public ButtonDescriptor setType(ResourceLocation type) {
        this.type = type;
        return this;
    }
}
