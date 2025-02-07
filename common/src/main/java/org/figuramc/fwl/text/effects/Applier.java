package org.figuramc.fwl.text.effects;

import org.figuramc.fwl.text.FWLStyle;

public interface Applier<V> {
    /**
     * Applies some effect to some value.
     * @param index index of character in the component.
     * @param length length of the component.
     * @return
     */
    V get(FWLStyle current, V initalValue, int index, int length);
}
