package org.figuramc.fwl.text.style;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.text.properties.Property;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * A field in a style. Here to reduce repetitive code caused by manually looping over values.
 */
public class StyleField<T> {

    private static final List<StyleField<?>> VALUES = new ArrayList<>();
    private static final AtomicInteger NEXT_INDEX = new AtomicInteger(0);

    public static final StyleField<Boolean> BOLD = new StyleField<>("bold", Property::parseBoolean, false);
    public static final StyleField<Boolean> ITALIC = new StyleField<>("italic", Property::parseBoolean, false);
    public static final StyleField<Boolean> OBFUSCATED = new StyleField<>("obfuscated", Property::parseBoolean, false);
    public static final StyleField<Boolean> STRIKETHROUGH = new StyleField<>("strikethrough", Property::parseBoolean, false);
    public static final StyleField<Boolean> UNDERLINE = new StyleField<>("underline", Property::parseBoolean, false);
    public static final StyleField<Boolean> SHADOW = new StyleField<>("shadow", Property::parseBoolean, false);
    public static final StyleField<Boolean> BACKGROUND = new StyleField<>("background", Property::parseBoolean, false);
    public static final StyleField<Boolean> OUTLINE = new StyleField<>("outline", Property::parseBoolean, false);

    public static final StyleField<Vector4f> COLOR = new StyleField<>("color", Property::parseColor, new Vector4f(1,1,1,1));
    public static final StyleField<Vector4f> BACKGROUND_COLOR = new StyleField<>("background_color", Property::parseColor, new Vector4f(0, 0, 0, 0));
    public static final StyleField<Vector4f> SHADOW_COLOR = new StyleField<>("shadow_color", Property::parseColor, new Vector4f(0, 0, 0,1));
    public static final StyleField<Vector4f> STRIKETHROUGH_COLOR = new StyleField<>("strikethrough_color", Property::parseColor, new Vector4f(1,1,1,1));
    public static final StyleField<Vector4f> UNDERLINE_COLOR = new StyleField<>("underline_color", Property::parseColor, new Vector4f(1,1,1,1));
    public static final StyleField<Vector4f> OUTLINE_COLOR = new StyleField<>("outline_color", Property::parseColor, new Vector4f(1,1,1,1));

    public static final StyleField<Vector2f> SCALE = new StyleField<>("scale", Property::parseVec2, new Vector2f(1, 1));
    public static final StyleField<Vector2f> OUTLINE_SCALE = new StyleField<>("outline_scale", Property::parseVec2, new Vector2f(1, 1));
    public static final StyleField<Vector2f> SKEW = new StyleField<>("skew", Property::parseVec2, new Vector2f(0, 0));
    public static final StyleField<Vector2f> OFFSET = new StyleField<>("offset", Property::parseVec2, new Vector2f(0, 0));
    public static final StyleField<Vector2f> SHADOW_OFFSET = new StyleField<>("shadow_offset", Property::parseVec2, new Vector2f(1, 1));

    public static final StyleField<Float> VERTICAL_ALIGNMENT = new StyleField<>("vertical_alignment", Property::parseFloat, 1f);
    public static final StyleField<ResourceLocation> FONT = new StyleField<>("font", Property::parseFont, new ResourceLocation("minecraft", "default"));

    public final String name;
    public final Function<JsonElement, Property<T>> parser;
    public final T defaultValue;

    public final int index;

    private StyleField(String name, Function<JsonElement, Property<T>> parser, T defaultValue) {
        this.name = name;
        this.parser = parser;
        this.defaultValue = defaultValue;
        this.index = NEXT_INDEX.getAndIncrement();
        VALUES.add(this);
    }

    public static int count() {
        return NEXT_INDEX.get();
    }

    public static StyleField<?> get(int index) {
        return VALUES.get(index);
    }

}
