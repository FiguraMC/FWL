package org.lexize.lwl.gui.widgets.descriptors;

import org.jetbrains.annotations.Nullable;
import org.lexize.lwl.LWL;
import org.lexize.lwl.gui.themes.LWLTheme;

public class BorderDescriptor {
    private float x, y, width, height;
    private Corner leftTop, rightTop, leftBottom, rightBottom;
    private boolean left, right, top, bottom;

    public BorderDescriptor(float x, float y, float width, float height) {
        this(x,y,width,height, (CornerType) null);
    }

    public BorderDescriptor(float x, float y, float width, float height, @Nullable CornerType corners) {
        this(x,y,width,height, corners != null ? new Corner(corners, LWL.peekTheme().defaultBorderRadius(), PathStyle.SOLID) : null);
    }

    public BorderDescriptor(float x, float y, float width, float height, @Nullable Corner corners) {
        this(x,y,width,height,corners,corners,corners,corners);
    }

    public BorderDescriptor(float x, float y, float width, float height, @Nullable Corner leftTop, @Nullable Corner rightTop, @Nullable Corner leftBottom, @Nullable Corner rightBottom) {
        LWLTheme currentTheme = LWL.peekTheme();
        float defaultWidth = currentTheme.defaultBorderRadius();
        CornerType defaultCornerType = currentTheme.defaultCornerType();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.leftTop = leftTop != null ? leftTop : new Corner(defaultCornerType, defaultWidth, PathStyle.SOLID);
        this.rightTop = rightTop != null ? rightTop : new Corner(defaultCornerType, defaultWidth, PathStyle.SOLID);
        this.leftBottom = leftBottom != null ? leftBottom : new Corner(defaultCornerType, defaultWidth, PathStyle.SOLID);
        this.rightBottom = rightBottom != null ? rightBottom : new Corner(defaultCornerType, defaultWidth, PathStyle.SOLID);
    }

    public float x() {
        return x;
    }

    public BorderDescriptor setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public BorderDescriptor setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public BorderDescriptor setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public BorderDescriptor setHeight(float height) {
        this.height = height;
        return this;
    }

    public Corner leftTop() {
        return leftTop;
    }

    public BorderDescriptor setLeftTop(Corner leftTop) {
        this.leftTop = leftTop;
        return this;
    }

    public Corner rightTop() {
        return rightTop;
    }

    public BorderDescriptor setRightTop(Corner rightTop) {
        this.rightTop = rightTop;
        return this;
    }

    public Corner leftBottom() {
        return leftBottom;
    }

    public BorderDescriptor setLeftBottom(Corner leftBottom) {
        this.leftBottom = leftBottom;
        return this;
    }

    public Corner rightBottom() {
        return rightBottom;
    }

    public BorderDescriptor setRightBottom(Corner rightBottom) {
        this.rightBottom = rightBottom;
        return this;
    }

    public boolean left() {
        return left;
    }

    public BorderDescriptor setLeft(boolean left) {
        this.left = left;
        return this;
    }

    public boolean right() {
        return right;
    }

    public BorderDescriptor setRight(boolean right) {
        this.right = right;
        return this;
    }

    public boolean top() {
        return top;
    }

    public BorderDescriptor setTop(boolean top) {
        this.top = top;
        return this;
    }

    public boolean bottom() {
        return bottom;
    }

    public BorderDescriptor setBottom(boolean bottom) {
        this.bottom = bottom;
        return this;
    }

    public enum CornerType {
        ROUND,
        RECTANGULAR
    }

    public record Corner(CornerType type, float radius, PathStyle style) {}
}
