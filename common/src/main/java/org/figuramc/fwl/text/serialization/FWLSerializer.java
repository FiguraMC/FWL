package org.figuramc.fwl.text.serialization;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.text.FWLCharSequence;
import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.text.components.LiteralComponent;
import org.figuramc.fwl.text.components.TranslatableComponent;
import org.figuramc.fwl.text.effects.*;
import org.figuramc.fwl.text.providers.headless.EffectProvider;
import org.figuramc.fwl.text.providers.headless.PropertyPair;
import org.figuramc.fwl.text.providers.headless.ProviderBuilder;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.figuramc.fwl.utils.JsonUtils.*;

public class FWLSerializer {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
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
            JsonObject componentObject = element.getAsJsonObject();
            EffectProvider styleProvider = parseStyleProvider(componentObject);
            component.setStyle(styleProvider);

            if (componentObject.has("extra")) {
                JsonArray extra = arrayOrThrow(componentObject, "extra");
                for (JsonElement elem: extra) {
                    AbstractComponent sibling = parse(elem);
                    component.append(sibling);
                }
            }
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
        FWLStyle style = FWLStyle.EMPTY;
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
            else if (fieldClass == AbstractComponent.class) {
                return parse(element);
            }
        }
        catch (Exception ignored) {

        }
        return null;
    }

    private static JsonElement valueToJson(Object object, Class<?> fieldClass) {
        if (fieldClass == Boolean.class) return new JsonPrimitive((Boolean) object);
        else if (fieldClass == Float.class) return new JsonPrimitive((Float) object);
        else if (fieldClass == Vector2f.class) {
            JsonArray arr = new JsonArray();
            Vector2f vec = (Vector2f) object;
            arr.add(vec.x);
            arr.add(vec.y);
            return arr;
        }
        else if (fieldClass == Vector4f.class) {
            JsonArray arr = new JsonArray();
            Vector4f vec = (Vector4f) object;
            arr.add(vec.x);
            arr.add(vec.y);
            arr.add(vec.z);
            arr.add(vec.w);
            return arr;
        }
        else if (fieldClass == ResourceLocation.class) return new JsonPrimitive(((ResourceLocation) object).toString());
        else if (fieldClass == AbstractComponent.class) return serialize((AbstractComponent) object);
        else throw new IllegalArgumentException("Expected %s, got %s".formatted(fieldClass.getSimpleName(), object.getClass().getSimpleName()));
    }

    @SuppressWarnings("unchecked")
    public static JsonElement expressionToJson(Applier<?> applier, Class<?> fieldClass) {
        if (applier instanceof ConstantApplier<?> constant) return valueToJson(constant.value(), fieldClass);
        else if (applier instanceof WithValue<?> withValue) {
            JsonElement expr = expressionToJson(withValue.applier(), fieldClass);
            if (expr.isJsonObject()) {
                JsonObject exprObject = expr.getAsJsonObject();
                exprObject.add("value", expressionToJson(withValue.value(), fieldClass));
            }
            return expr;
        }
        else {
            String type = applier.getType();
            EffectSerializer<Applier<Object>> serializer = (EffectSerializer<Applier<Object>>) EFFECT_SERIALIZERS.get(type);
            if (serializer == null) throw new IllegalStateException("No serializer found for effect %s".formatted(type));
            JsonElement expr = serializer.serialize((Applier<Object>) applier);
            if (expr.isJsonObject()) {
                expr.getAsJsonObject().addProperty("type", type);
            }
            return expr;
        }
    }

    @SuppressWarnings("unchecked")
    public static JsonObject serialize(AbstractComponent component) {
        String type = component.type();
        ComponentSerializer<AbstractComponent> serializer =
                (ComponentSerializer<AbstractComponent>) COMPONENT_SERIALIZERS.get(type);
        if (serializer == null) throw new IllegalStateException("No serializer found for type %s".formatted(type));
        JsonObject object = serializer.serialize(component);
        object.addProperty("type", type);

        EffectProvider effect = component.getStyle();
        if (effect != null) {
            FWLStyle style = effect.style();
            for (FWLStyle.Property<?> prop: FWLStyle.PROPERTIES) {
                List<Applier<?>> appliers = effect.effects().stream().filter(p -> p.value() == prop)
                        .map(PropertyPair::applier).collect(Collectors.toUnmodifiableList());

                JsonElement fieldValue;

                if (prop.has(style)) {
                    fieldValue = valueToJson(prop.get(style), prop.fieldType());
                }
                else fieldValue = null;

                if (appliers.isEmpty()) {
                    if (fieldValue != null) {
                        object.add(prop.fieldName(), fieldValue);
                    }
                }
                else {
                    if (appliers.size() == 1) {
                        JsonElement expr = expressionToJson(appliers.get(0), prop.fieldType());
                        if (expr.isJsonObject()) {
                            JsonObject exprObject = expr.getAsJsonObject();
                            if (!exprObject.has("value") && fieldValue != null) exprObject.add("value", fieldValue);
                        }
                        object.add(prop.fieldName(), expr);
                    }
                    else {
                        JsonObject fieldObject = new JsonObject();
                        if (fieldValue != null) fieldObject.add("value", fieldValue);
                        JsonArray effects = new JsonArray();
                        for (Applier<?> applier: appliers) {
                            effects.add(expressionToJson(applier, prop.fieldType()));
                        }
                        fieldObject.add("effects", effects);
                        object.add(prop.fieldName(), fieldObject);
                    }
                }
            }
        }

        if (!component.siblings().isEmpty()) {
            JsonArray array = new JsonArray(component.siblings().size());
            for (AbstractComponent sibling: component.siblings()) {
                array.add(serialize(sibling));
            }
            object.add("extra", array);
        }

        return object;
    }

    public static String toJson(AbstractComponent component) {
        JsonObject componentJson = serialize(component);
        return GSON.toJson(componentJson);
    }

    static {
        registerComponentSerializer("text", LiteralComponent::serialize);
        registerComponentSerializer("translatable", TranslatableComponent::serialize);

        registerComponentDeserializer("text", LiteralComponent::deserialize);
        registerComponentDeserializer("translatable", TranslatableComponent::deserialize);

        registerEffectSerializer("gradient", GradientApplier::serialize);
        registerEffectSerializer("shake", ShakeApplier::serialize);

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
