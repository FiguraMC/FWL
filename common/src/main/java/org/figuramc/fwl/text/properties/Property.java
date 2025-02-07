package org.figuramc.fwl.text.properties;

import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.fomponents.BaseFomponent;

public interface Property<T> {

    // Compile this into a compiled style property.
    CompiledStyle.CompiledProperty<T> compile(int currentIndex, BaseFomponent fomponent);

    // Whether this varies with respect to its args to compile().
    // If all properties have this as false, optimization can be done.
    boolean varies();
}
