package org.figuramc.fwl.gui.widgets.descriptors.misc;

import net.minecraft.resources.ResourceLocation;

public class IconDescriptor {
    private float x, y, width, height;
    private ResourceLocation iconPath;

    public IconDescriptor(float x, float y, float width, float height, ResourceLocation iconPath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.iconPath = iconPath;
    }

    public float x() {
        return x;
    }

    public IconDescriptor setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public IconDescriptor setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public IconDescriptor setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public IconDescriptor setHeight(float height) {
        this.height = height;
        return this;
    }

    public ResourceLocation iconPath() {
        return iconPath;
    }

    public IconDescriptor setIconPath(ResourceLocation iconPath) {
        this.iconPath = iconPath;
        return this;
    }
}
