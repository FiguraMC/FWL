package org.figuramc.fwl.text.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.text.components.LiteralComponent;
import org.figuramc.fwl.text.effects.*;
import org.figuramc.fwl.text.providers.headless.EffectProvider;
import org.figuramc.fwl.text.providers.headless.PropertyPair;
import org.figuramc.fwl.text.providers.headless.ProviderBuilder;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static org.figuramc.fwl.utils.JsonUtils.stringOrDefault;
import static org.figuramc.fwl.utils.JsonUtils.stringOrThrow;

public class FWLSerializer {
    private static final Gson GSON = new Gson();
    private static final HashMap<String, ComponentSerializer<?>> COMPONENT_SERIALIZERS = new HashMap<>();
    private static final HashMap<String, ComponentDeserializer<?>> COMPONENT_DESERIALIZERS = new HashMap<>();
    private static final HashMap<String, EffectSerializer<?>> EFFECT_SERIALIZERS = new HashMap<>();
    private static final HashMap<String, EffectDeserializer<?>> EFFECT_DESERIALIZERS = new HashMap<>();
    private static final HashMap<String, String> FIELD_TO_TYPE = new HashMap<>();

    public static <C extends AbstractComponent> void registerComponentSerializer(String type, ComponentSerializer<C> serializer) {
        if (COMPONENT_SERIALIZERS.containsKey(type)) throw new IllegalArgumentException("Serializer for type %s already exists".formatted(type));
        COMPONENT_SERIALIZERS.put(type, serializer);
    }

    public static <C extends AbstractComponent> void registerComponentDeserializer(String type, ComponentDeserializer<C> deserializer) {
        if (COMPONENT_DESERIALIZERS.containsKey(type)) throw new IllegalArgumentException("Deserializer for type %s already exists".formatted(type));
        COMPONENT_DESERIALIZERS.put(type, deserializer);
    }

    public static <V extends Applier<?>> void registerEffectSerializer(String type, EffectSerializer<V> serializer) {
        if (EFFECT_SERIALIZERS.containsKey(type)) throw new IllegalArgumentException("Serializer for type %s already exists".formatted(type));
        EFFECT_SERIALIZERS.put(type, serializer);
    }

    public static <V extends Applier<?>> void registerEffectDeserializer(String type, EffectDeserializer<V> deserializer) {
        if (EFFECT_DESERIALIZERS.containsKey(type)) throw new IllegalArgumentException("Deserializer for type %s already exists".formatted(type));
        EFFECT_DESERIALIZERS.put(type, deserializer);
    }

    public static <C extends AbstractComponent> void registerFieldToType(String field, String type) {
        if (FIELD_TO_TYPE.containsKey(type)) throw new IllegalArgumentException("Type for field %s is already registered".formatted(field));
        FIELD_TO_TYPE.put(field, type);
    }

    public static String getComponentType(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject componentObject = element.getAsJsonObject();
            String type = stringOrDefault(componentObject, "type", null);
            if (type != null) return type;
            else {
                for (String key: componentObject.keySet()) {
                    if (FIELD_TO_TYPE.containsKey(key)) return FIELD_TO_TYPE.get(key);
                }
            }
        }
        else if (element.isJsonArray()) return "container";
        else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) return "text";
        throw new IllegalArgumentException("Unable to determine the type of the component");
    }

    public static AbstractComponent parse(String json) {
        return parse(GSON.fromJson(json, JsonElement.class));
    }

    public static AbstractComponent parse(JsonElement element) {
        String type = getComponentType(element);

        if (type.equals("container")) return parseContainer(element.getAsJsonArray());
        ComponentDeserializer<?> deserializer = COMPONENT_DESERIALIZERS.get(type);
        if (deserializer == null) throw new IllegalStateException("No deserializer found for type %s".formatted(type));

        AbstractComponent component = deserializer.deserialize(element);
        if (element.isJsonObject()) {
            EffectProvider styleProvider = parseStyleProvider(element.getAsJsonObject());
            component.setStyle(styleProvider);
        }
        return component;
    }

    private static AbstractComponent parseContainer(JsonArray components) {
        if (components.isEmpty()) return new LiteralComponent("");
        AbstractComponent firstComponent = parse(components.get(0));
        for (int i = 1; i < components.size(); i++) {
            firstComponent.append(parse(components.get(i)));
        }
        return firstComponent;
    }

    @SuppressWarnings("unchecked")
    public static EffectProvider parseStyleProvider(JsonObject styleObject) {
        FWLStyle style = FWLStyle.empty();
        ProviderBuilder builder = new ProviderBuilder(style);
        for (FWLStyle.Property<?> property: FWLStyle.PROPERTIES) {
            FWLStyle.Property<Object> prop = (FWLStyle.Property<Object>) property;
            String fieldName = prop.fieldName();
            if (styleObject.has(fieldName)) {
                JsonElement element = styleObject.get(fieldName);
                Object defaultValue = readDefaultValue(element, prop.fieldType());
                if (defaultValue != null) prop.set(style, defaultValue);
                if (element.isJsonObject()) {
                    JsonObject fieldObject = element.getAsJsonObject();
                    if (fieldObject.has("type")) {
                        if (defaultValue != null) fieldObject.remove("value");
                        Applier<Object> expression = (Applier<Object>) parseExpression(fieldObject, prop.fieldType());
                        builder.withEffect(prop, expression);
                    }
                    else if (fieldObject.has("effects")) {
                        parseEffects(fieldObject.get("effects").getAsJsonArray(), property, builder, prop.fieldType());
                    }
                }
            }
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private static void parseEffects(JsonArray effectsArray, FWLStyle.Property<?> property, ProviderBuilder builder, Class<?> fieldType) {
        for (JsonElement value: effectsArray) {
            Applier<Object> expression = (Applier<Object>) parseExpression(value, fieldType);
            builder.withEffect((FWLStyle.Property<Object>) property, expression);
        }
    }

    @SuppressWarnings("unchecked")
    public static Applier<?> parseExpression(JsonElement element, Class<?> fieldClass) {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            Applier<Object> initialValueExpression = object.has("value") ? (Applier<Object>) parseExpression(object.get("value"), fieldClass) : null;
            String type = stringOrThrow(object, "type");
            if (initialValueExpression != null && type.equals("expr")) return initialValueExpression;
            else {
                EffectDeserializer<?> deserializer = EFFECT_DESERIALIZERS.get(type);
                if (deserializer == null) throw new IllegalStateException("No deserializer found for effect %s".formatted(type));
                Applier<?> actualApplier = deserializer.deserialize(object, fieldClass);
                if (initialValueExpression != null) return new WithValue<>((Applier<Object>) actualApplier, initialValueExpression);
                else return actualApplier;
            }
        }
        else {
            Object value = readValue(element, fieldClass);
            if (value != null) return ConstantApplier.constant(value);
            else throw new IllegalArgumentException("Unable to parse expression");
        }
        // Object value = readDefaultValue(element, fieldClass);
        // if (value != null) {
        //     Applier<Object> valueApplier = ConstantApplier.constant(value);
        //     if (element.isJsonObject()) {
        //         JsonObject object = element.getAsJsonObject();
        //         object.remove("value");
        //         Applier<?> actualExpression = parseExpression(element, fieldClass);
        //         return new WithValue<>((Applier<Object>) actualExpression, valueApplier);
        //     }
        //     else return valueApplier;
        // }
        // else {
        //     JsonObject object = element.getAsJsonObject();
        //     String type = stringOrThrow(object, "type");
        //     EffectDeserializer<?> deserializer = EFFECT_DESERIALIZERS.get(type);
        //     if (deserializer == null) throw new IllegalStateException("No deserializer found for effect %s".formatted(type));
        //     return deserializer.deserialize(element, fieldClass);
        // }
    }

    private static Object readDefaultValue(JsonElement element, Class<?> fieldClass) {
        Object value = readValue(element, fieldClass);
        if (value != null) return value;
        else if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            return object.has("value") ? readValue(object.get("value"), fieldClass) : null;
        }
        return null;
    }

    private static Object readValue(JsonElement element, Class<?> fieldClass) {
        try {
            if (fieldClass == Boolean.class) return element.getAsBoolean();
            else if (fieldClass == Float.class) return element.getAsFloat();
            else if (fieldClass == Vector2f.class) {
                JsonArray array = element.getAsJsonArray();
                return new Vector2f(array.get(0).getAsFloat(), array.get(1).getAsFloat());
            }
            else if (fieldClass == Vector4f.class) {
                if (element.isJsonPrimitive()) {
                    String colorString = element.getAsString();
                    TextColor color = TextColor.parseColor(colorString);
                    if (color != null) {
                        int rgb = color.getValue();
                        return new Vector4f(
                                FastColor.ARGB32.red(rgb) / 255f,
                                FastColor.ARGB32.green(rgb) / 255f,
                                FastColor.ARGB32.blue(rgb) / 255f,
                                1
                        );
                    }
                    else return new Vector4f(1);
                }
                else {
                    JsonArray array = element.getAsJsonArray();
                    return new Vector4f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat(), array.get(3).getAsFloat());
                }
            }
            else if (fieldClass == ResourceLocation.class) {
                String resLocString = element.getAsString();
                return ResourceLocation.tryParse(resLocString);
            }
        }
        catch (Exception ignored) {

        }
        return null;
    }

    static {
        registerComponentSerializer("text", LiteralComponent::serialize);

        registerComponentDeserializer("text", LiteralComponent::deserialize);

        registerEffectDeserializer("gradient", GradientApplier::deserialize);
        registerEffectDeserializer("shake", ShakeApplier::deserialize);

        registerFieldToType("text", "text");
        registerFieldToType("translate", "translatable");
        registerFieldToType("score", "score");
        registerFieldToType("selector", "translatable");
        registerFieldToType("keybind", "keybind");
        registerFieldToType("nbt", "nbt");
    }
}
