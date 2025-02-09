package org.figuramc.fwl.text.fomponents;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.figuramc.fwl.text.FomponentVisitor;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.style.FomponentStyle;
import org.figuramc.fwl.utils.JsonUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class BaseFomponent {

    private final FomponentStyle style;
    private final ArrayList<BaseFomponent> siblings;
    private int length = -1;

    public BaseFomponent() {
        this(FomponentStyle.style());
    }
    public BaseFomponent(FomponentStyle style) {
        this(style, new ArrayList<>());
    }
    public BaseFomponent(FomponentStyle style, ArrayList<BaseFomponent> siblings) {
        this.style = style;
        this.siblings = siblings;
    }

    protected abstract int selfLength();
    protected abstract int selfVisit(FomponentVisitor visitor, CompiledStyle style, int currentIndex); // Return the new index!
    protected abstract JsonObject getBaseJsonObject(); // Get the json object for just this text, not taking into account siblings or style.

    @Contract("_->this")
    public BaseFomponent append(BaseFomponent sibling) {
        siblings.add(sibling);
        this.length = -1; // Invalidate length cache
        return this;
    }

    public int length() {
        // If cached length, reuse, otherwise compute
        if (length == -1) {
            length = selfLength();
            for (BaseFomponent sibling : siblings)
                length += sibling.length;
        }
        return length;
    }

    public void visit(FomponentVisitor visitor) {
        this.visit(visitor, CompiledStyle.EMPTY, 0);
    }

    private int visit(FomponentVisitor visitor, CompiledStyle parentStyle, int currentIndex) {
        CompiledStyle compiled = parentStyle.extend(this.style.compile(currentIndex, this));
        currentIndex = selfVisit(visitor, compiled, currentIndex);
        for (BaseFomponent sibling : siblings)
            currentIndex = sibling.visit(visitor, compiled, currentIndex);
        return currentIndex;
    }

    public final JsonElement toJson() {
        // Get the base json object
        JsonObject json = getBaseJsonObject();
        // Add style
        style.writeJson(json);
        // Add siblings
        JsonArray extras = new JsonArray();
        for (BaseFomponent sibling : siblings)
            extras.add(sibling.toJson());
        if (!extras.isEmpty()) json.add("extras", extras);
        // Return
        return json;
    }

    public static BaseFomponent fromJson(JsonElement json) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            return new LiteralFomponent(json.getAsString());
        } else if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            BaseFomponent base = fromJson(array.get(0));
            for (int i = 1; i < array.size(); i++)
                base.append(fromJson(array.get(i)));
            return base;
        } else if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            // Get style
            FomponentStyle style = FomponentStyle.readJson(obj);
            // Get extras
            ArrayList<BaseFomponent> extras = new ArrayList<>();
            JsonArray extrasJson = JsonUtils.arrayOrNull(obj, "extras");
            if (extrasJson != null)
                for (JsonElement element : extrasJson)
                    extras.add(fromJson(element));
            // Get main text body
            @Nullable String type = JsonUtils.stringOrDefault(obj, "type", null);
            if ("text".equals(type) || type == null && obj.has("text")) {
                String text = JsonUtils.stringOrDefault(obj, "text", null);
                if (text == null) throw new IllegalArgumentException("Expected \"text\" key");
                return new LiteralFomponent(text, style, extras);
            } else if ("translatable".equals(type) || type == null && obj.has("translate")) {
                throw new UnsupportedOperationException("Translation not supported yet");
            } else if ("score".equals(type) || type == null && obj.has("score")) {
                throw new UnsupportedOperationException("Score not supported yet");
            } else if ("selector".equals(type) || type == null && obj.has("selector")) {
                throw new UnsupportedOperationException("Selector not supported yet");
            } else if ("keybind".equals(type) || type == null && obj.has("keybind")) {
                throw new UnsupportedOperationException("Keybind not supported yet");
            } else if ("nbt".equals(type) || type == null && obj.has("nbt")) {
                throw new UnsupportedOperationException("Nbt not supported yet");
            } else {
                throw new IllegalArgumentException("Failed to parse json component - unknown text type");
            }
        } else {
            throw new IllegalArgumentException("Failed to parse json component - expected string, array, or object");
        }
    }


}
