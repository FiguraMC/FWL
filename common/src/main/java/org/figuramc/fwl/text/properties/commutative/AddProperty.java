package org.figuramc.fwl.text.properties.commutative;

import com.google.gson.JsonElement;
import org.figuramc.fwl.text.properties.Property;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class AddProperty<T> extends CommutativeProperty<T> {

    public AddProperty(List<Property<T>> values, Class<T> tClass) {
        super(values, tClass);
    }

    @Override protected String id() { return "add"; }

    @Override protected boolean unitBool() { return false; }
    @Override protected float unitFloat() { return 0; }
    @Override protected Vector2f unitVec2() { return new Vector2f(); }
    @Override protected Vector3f unitVec3() { return new Vector3f(); }
    @Override protected Vector4f unitVec4() { return new Vector4f(); }

    @Override protected boolean operate(boolean left, boolean right) { return left || right; }
    @Override protected float operate(float left, float right) { return left + right; }
    @Override protected Vector2f operate(Vector2f left, Vector2f right) { return left.add(right, new Vector2f()); }
    @Override protected Vector3f operate(Vector3f left, Vector3f right) { return left.add(right, new Vector3f()); }
    @Override protected Vector4f operate(Vector4f left, Vector4f right) { return left.add(right, new Vector4f()); }

}
