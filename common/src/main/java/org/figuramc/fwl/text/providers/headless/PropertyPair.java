package org.figuramc.fwl.text.providers.headless;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.effects.Applier;

public record PropertyPair<V>(FWLStyle.Property<V> value, Applier<V> applier) {
}
