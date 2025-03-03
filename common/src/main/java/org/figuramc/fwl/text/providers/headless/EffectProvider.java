package org.figuramc.fwl.text.providers.headless;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.effects.Applier;

import java.util.ArrayList;


public class EffectProvider implements HeadlessStyleProvider {
    private final FWLStyle style;
    private final ArrayList<PropertyPair<?>> effects;

    public EffectProvider(FWLStyle style, ArrayList<PropertyPair<?>> effects) {
        this.style = style;
        this.effects = effects;
    }


    @Override
    public FWLStyle get(int index, int length) {
        if (effects.isEmpty()) return style;
        else {
            FWLStyle current = style.clone();
            for (PropertyPair<?> pair: effects) {
                current = apply(current, pair, index, length);
            }
            return current;
        }
    }

    private <V> FWLStyle apply(FWLStyle current, PropertyPair<V> pair, int index, int length) {
        FWLStyle.Property<V> property = pair.value();
        Applier<V> applier = pair.applier();
        V initialValue = property.get(current);
        return property.set(current, applier.get(current, initialValue, index, length));
    }

    public FWLStyle style() {
        return style;
    }

    public ArrayList<PropertyPair<?>> effects() {
        return effects;
    }
}
