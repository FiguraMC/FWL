package org.figuramc.fwl.utils;

import com.mojang.math.MatrixUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public record Rectangle(float x, float y, float width, float height) {
    private static final Matrix4f IDENTITY_MAT = new Matrix4f();

    public static Rectangle intersection(Rectangle a, Rectangle b) {
        float x0 = Math.max(a.x, b.x);
        float y0 = Math.max(a.y, b.y);
        float x1 = Math.min(a.right(), b.right());
        float y1 = Math.min(a.bottom(), b.bottom());

        float width = Math.max(x1 - x0, 0);
        float height = Math.max(y1 - y0, 0);

        return new Rectangle(x0, y0, width, height);
    }

    public float left() {
        return x;
    }

    public float right() {
        return x + width;
    }

    public float top() {
        return y;
    }

    public float bottom() {
        return y + height;
    }

    public static Rectangle withMat(float x, float y, float width, float height, Matrix4f mat) {
        Vector3f a = new Vector3f(x, y, 0);
        Vector3f b = new Vector3f(x + width, y + height, 0);
        a.mulPosition(mat);
        b.mulPosition(mat);
        float x0 = a.x;
        float y0 = a.y;
        float x1 = b.x;
        float y1 = b.y;
        float w = Math.max(0, x1 - x0);
        float h = Math.max(0, y1 - y0);
        return new Rectangle(x0, y0, w, h);
    }

    public Rectangle transformed(Matrix4f pose) {
        if (pose.equals(IDENTITY_MAT)) return this;
        Vector3f pos1 = pose.transformPosition(left(), top(), 0.0f, new Vector3f());
        Vector3f pos2 = pose.transformPosition(right(), bottom(), 0.0f, new Vector3f());
        return new Rectangle(pos1.x, pos1.y, pos2.x - pos1.x, pos2.y - pos1.y);
    }

    public boolean pointIn(float x, float y) {
        return x >= left() && x <= right() && y >= top() && y <= bottom();
    }

    public boolean pointIn(double x, double y) {
        return x >= left() && x <= right() && y >= top() && y <= bottom();
    }
}
