package org.figuramc.fwl.text.providers.headless;

import org.figuramc.fwl.text.Property;
import org.figuramc.fwl.text.effects.Applier;

public record PropertyPair<V>(Property<V> value, Applier<V> applier) {
}
