package org.figuramc.fwl.text.providers.headless;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.effects.Applier;

import java.util.ArrayList;

public class ProviderBuilder {
    private FWLStyle style;

    private ArrayList<PropertyPair<?>> effects = new ArrayList<>();

    public ProviderBuilder(FWLStyle style) {
        this.style = style;
    }

    public ProviderBuilder() {
        this.style = FWLStyle.EMPTY;
    }

    public <V> ProviderBuilder with(FWLStyle.Property<V> property, V value) {
        style = property.with(style, value);
        return this;
    }

    public <V> ProviderBuilder withEffect(FWLStyle.Property<V> property, Applier<V> applier) {
        effects.add(new PropertyPair<>(property, applier));
        return this;
    }

    public EffectProvider build() {
        EffectProvider provider = new EffectProvider(style, effects);
        effects = new ArrayList<>();
        return provider;
    }
}
