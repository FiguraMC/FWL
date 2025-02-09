package org.figuramc.fwl.text.components;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.locale.Language;
import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.providers.headless.EffectProvider;
import org.figuramc.fwl.text.serialization.FWLSerializer;
import org.figuramc.fwl.text.sinks.OffsetSink;
import org.figuramc.fwl.text.sinks.StyledSink;
import org.figuramc.fwl.utils.Either;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.figuramc.fwl.utils.JsonUtils.*;

public class TranslatableComponent extends AbstractComponent {
    private AbstractComponent[] args;
    private String key;
    private @Nullable String fallback;

    private int bakedHash;
    private Either<AbstractComponent, String>[] bakedText;
    private int bakedLength;
    private Language bakeLanguage;

    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TranslatableComponent(String key, AbstractComponent... args) {
        this(key, null, args);
    }

    public TranslatableComponent(String key, @Nullable String fallback, AbstractComponent... args) {
        this.key = Objects.requireNonNull(key, "Translation key can't be null");
        this.fallback = fallback;
        this.args = args == null ? new AbstractComponent[0] : args;
    }

    @Override
    protected int selfLength() {
        bakeText();
        return bakedLength;
    }

    @Override
    protected boolean visitSelf(StyledSink sink) {
        bakeText();
        int offset = 0;
        for (var text: bakedText) {
            if (text.isA()) {
                var component = text.getA();
                if (!component.visit(new OffsetSink(this::getSelfStyle, sink, offset))) return false;
                offset += component.length();
            }
            else {
                String component = text.getB();
                IntStream codepointStream = component.chars();
                Iterator<Integer> iterator = codepointStream.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    if (!sink.accept(i + offset, this::getSelfStyle, iterator.next())) return false;
                    i += 1;
                }
                offset += component.length();
            }
        }
        return true;
    }

    private void bakeText() {
        Language language = Language.getInstance();
        int currentHash = Objects.hash(Arrays.hashCode(args), key, fallback);
        if (bakeLanguage != language || bakedHash != currentHash) {
            String value = fallback != null ? language.getOrDefault(key, fallback) : language.getOrDefault(key);

            bakedText = decompose(value);
            bakedLength = 0;
            for (var text: bakedText) {
                bakedLength += text.isA() ? text.getA().length() : text.getB().length();
            }

            bakedHash = currentHash;
            bakeLanguage = language;
        }
    }

    @SuppressWarnings("unchecked")
    private Either<AbstractComponent, String>[] decompose(String value) {
        ArrayList<Either<AbstractComponent, String>> list = new ArrayList<>();

        Matcher argumentMatcher = ARGUMENT_PATTERN.matcher(value);

        int argIndex = 0;

        int start = 0;
        int end;
        while (argumentMatcher.find(start)) {
            String type = argumentMatcher.group(2);
            end = argumentMatcher.start();
            if ("s".equals(type)) {
                if (end - start > 0) {
                    list.add(Either.ofB(value.substring(start, end)));
                }

                int aInd;

                String index = argumentMatcher.group(1);
                if (index != null) aInd = Integer.parseInt(index);
                else aInd = argIndex++;

                if (aInd - 1 > args.length) list.add(Either.ofB("null"));
                else {
                    AbstractComponent component = args[aInd-1];
                    if (component == null) list.add(Either.ofB("null"));
                    else list.add(Either.ofA(component));
                }
            }
            else if ("%%".equals(argumentMatcher.group())) {
                list.add(Either.ofB("%"));
            }
            else throw new IllegalArgumentException("Invalid type of argument at position %s".formatted(end));

            start = argumentMatcher.end();
        }

        if (start < value.length()) {
            list.add(Either.ofB(value.substring(start)));
        }

        return list.toArray(new Either[0]);
    }

    public List<AbstractComponent> getArgs() {
        return ImmutableList.copyOf(args);
    }

    public TranslatableComponent setArgs(AbstractComponent... args) {
        this.args = args == null ? new AbstractComponent[0] : args;
        return this;
    }

    public TranslatableComponent setKey(String key) {
        this.key = Objects.requireNonNull(key, "Translation key can't be null");
        return this;
    }

    public String getKey() {
        return key;
    }

    public TranslatableComponent setFallback(@Nullable String fallback) {
        this.fallback = fallback;
        return this;
    }

    public @Nullable String getFallback() {
        return fallback;
    }

    @Override
    public TranslatableComponent setStyle(FWLStyle style) {
        super.setStyle(style);
        return this;
    }

    @Override
    public TranslatableComponent setStyle(EffectProvider provider) {
        super.setStyle(provider);
        return this;
    }

    @Override
    public TranslatableComponent append(AbstractComponent other) {
        super.append(other);
        return this;
    }

    @Override
    public String type() {
        return "translatable";
    }

    public static TranslatableComponent translation(String key) {
        return new TranslatableComponent(key);
    }

    public static TranslatableComponent translation(String key, AbstractComponent... args) {
        return new TranslatableComponent(key, args);
    }

    public static TranslatableComponent translation(String key, String fallback) {
        return new TranslatableComponent(key, fallback);
    }

    public static TranslatableComponent translation(String key, String fallback, AbstractComponent... args) {
        return new TranslatableComponent(key, fallback, args);
    }

    public static JsonObject serialize(TranslatableComponent component) {
        JsonObject object = new JsonObject();
        object.addProperty("translate", component.key);
        if (component.fallback != null) object.addProperty("fallback", component.fallback);
        if (component.args.length > 0) {
            JsonArray with = new JsonArray();
            for (AbstractComponent arg: component.args) {
                with.add(FWLSerializer.serialize(arg));
            }
            object.add("with", with);
        }
        return object;
    }

    public static TranslatableComponent deserialize(JsonElement element) {
        JsonObject translationObject = element.getAsJsonObject();
        String translationKey = stringOrThrow(translationObject, "translate");
        String fallback = stringOrDefault(translationObject, "fallback", null);
        JsonArray with = arrayOrDefault(translationObject, "with", null);
        AbstractComponent[] args;
        if (with != null) {
            ArrayList<AbstractComponent> withList = new ArrayList<>();
            for (JsonElement elem: with) {
                withList.add(FWLSerializer.parse(elem));
            }
            args = withList.toArray(new AbstractComponent[0]);
        }
        else {
            args = new AbstractComponent[0];
        }

        return new TranslatableComponent(translationKey, fallback, args);
    }
}
