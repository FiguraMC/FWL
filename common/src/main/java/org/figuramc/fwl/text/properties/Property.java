package org.figuramc.fwl.text.properties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.text.properties.commutative.AddProperty;
import org.figuramc.fwl.text.properties.commutative.CommutativeProperty;
import org.figuramc.fwl.text.properties.special.AlternatingProperty;
import org.figuramc.fwl.text.properties.special.GradientProperty;
import org.figuramc.fwl.text.properties.special.RandomProperty;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.fomponents.BaseFomponent;
import org.figuramc.fwl.text.style.FomponentStyle;
import org.figuramc.fwl.utils.ColorUtils;
import org.figuramc.fwl.utils.JsonUtils;
import org.joml.Vector2f;
import org.joml.Vector4f;

public interface Property<T> {

    // Compile this into a compiled style property.
    CompiledStyle.CompiledProperty<T> compile(int currentIndex, BaseFomponent fomponent);

    // Whether the CompiledProperty yielded by this is cacheable.
    // If all properties in a style have this as true, an optimization can be done.
    boolean cacheable();

    // Convert this to a json element.
    JsonElement toJson();

    // Parse from json element.
    // If arg is invalidly formatted or null, throw an error.
    static Property<Boolean> parseBoolean(JsonElement elem) throws IllegalArgumentException {
        if (elem == null)
            throw new IllegalArgumentException("Unrecognized value for boolean property: expected boolean or object");
        if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isBoolean())
            return FomponentStyle.constant(elem.getAsBoolean());
        if (elem.isJsonObject()) {
            JsonObject obj = elem.getAsJsonObject();
            String type = JsonUtils.stringOrDefault(obj, "type", null);
            if (type == null) throw new IllegalArgumentException("Type of {} property cannot be null");
            return switch (type) {
                case "alternating" -> AlternatingProperty.INSTANCE;
                case "random" -> RandomProperty.fromJson(obj, Property::parseBoolean, Boolean.class);
                case "gradient" -> GradientProperty.fromJson(obj, Property::parseBoolean, Boolean.class);
                case "add" -> CommutativeProperty.fromJson(obj, Property::parseBoolean, Boolean.class, "add", AddProperty::new);
                default -> throw new IllegalArgumentException("Unrecognized boolean property type \"" + elem.getAsString() + "\"");
            };
        }
        throw new IllegalArgumentException("Unrecognized value for boolean property: expected boolean or object");
    }
    static Property<Float> parseFloat(JsonElement elem) throws IllegalArgumentException {
        if (elem == null)
            throw new IllegalArgumentException("Unrecognized value for float property: expected number or object");
        if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isNumber())
            return FomponentStyle.constant(elem.getAsFloat());
        if (elem.isJsonObject()) {
            JsonObject obj = elem.getAsJsonObject();
            String type = JsonUtils.stringOrDefault(obj, "type", null);
            if (type == null) throw new IllegalArgumentException("Type of {} property cannot be null");
            return switch (type) {
                case "random" -> RandomProperty.fromJson(obj, Property::parseFloat, Float.class);
                case "gradient" -> GradientProperty.fromJson(obj, Property::parseFloat, Float.class);
                case "add" -> CommutativeProperty.fromJson(obj, Property::parseFloat, Float.class, "add", AddProperty::new);
                default -> throw new IllegalArgumentException("Unrecognized float property type \"" + elem.getAsString() + "\"");
            };
        }
        throw new IllegalArgumentException("Unrecognized value for float property: expected number or object");
    }
    static Property<Vector2f> parseVec2(JsonElement elem) {
        if (elem == null)
            throw new IllegalArgumentException("Unrecognized value for vec2 property: expected array of numbers or object");
        if (JsonUtils.isVec(elem, 2))
            return FomponentStyle.constant(new Vector2f(elem.getAsJsonArray().get(0).getAsFloat(), elem.getAsJsonArray().get(1).getAsFloat()));
        if (elem.isJsonObject()) {
            JsonObject obj = elem.getAsJsonObject();
            String type = JsonUtils.stringOrDefault(obj, "type", null);
            if (type == null) throw new IllegalArgumentException("Type of {} property cannot be null");
            return switch (type) {
                case "random" -> RandomProperty.fromJson(obj, Property::parseVec2, Vector2f.class);
                case "gradient" -> GradientProperty.fromJson(obj, Property::parseVec2, Vector2f.class);
                case "add" -> CommutativeProperty.fromJson(obj, Property::parseVec2, Vector2f.class, "add", AddProperty::new);
                default -> throw new IllegalArgumentException("Unrecognized vec2 property type \"" + elem.getAsString() + "\"");
            };
        }
        throw new IllegalArgumentException("Unrecognized value for vec2 property: expected array of numbers or object");
    }
    static Property<Vector4f> parseColor(JsonElement elem) {
        if (elem == null)
            throw new IllegalArgumentException("Unrecognized value for color property: expected string, array of numbers, or object");
        if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString())
            return FomponentStyle.constant(ColorUtils.fromString(elem.getAsString()));
        if (JsonUtils.isVec(elem, 4))
            return FomponentStyle.constant(elem.getAsJsonArray().get(0).getAsFloat(), elem.getAsJsonArray().get(1).getAsFloat(), elem.getAsJsonArray().get(2).getAsFloat(), elem.getAsJsonArray().get(3).getAsFloat());
        if (elem.isJsonObject()) {
            JsonObject obj = elem.getAsJsonObject();
            String type = JsonUtils.stringOrDefault(obj, "type", null);
            if (type == null) throw new IllegalArgumentException("Type of {} property cannot be null");
            return switch (type) {
                case "random" -> RandomProperty.fromJson(obj, Property::parseColor, Vector4f.class);
                case "gradient" -> GradientProperty.fromJson(obj, Property::parseColor, Vector4f.class);
                case "add" -> CommutativeProperty.fromJson(obj, Property::parseColor, Vector4f.class, "add", AddProperty::new);
                default -> throw new IllegalArgumentException("Unrecognized color property type \"" + elem.getAsString() + "\"");
            };
        }
        throw new IllegalArgumentException("Unrecognized value for color property: expected string, array of numbers, or object");
    }
    static Property<ResourceLocation> parseFont(JsonElement elem) {
        if (elem == null)
            throw new IllegalArgumentException("Unrecognized value for font property: expected string or object");
        if (elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) {
            ResourceLocation loc = ResourceLocation.tryParse(elem.getAsString());
            if (loc == null) throw new IllegalArgumentException("Font string must be a valid resource location, e.g. \"minecraft:default\"");
            return FomponentStyle.constant(loc);
        } if (elem.isJsonObject()) {
            JsonObject obj = elem.getAsJsonObject();
            String type = JsonUtils.stringOrDefault(obj, "type", null);
            if (type == null) throw new IllegalArgumentException("Type of {} property cannot be null");
            throw new IllegalArgumentException("Unrecognized font property type \"" + elem.getAsString() + "\"");
        }
        throw new IllegalArgumentException("Unrecognized value for font property: expected string or object");
    }

}
