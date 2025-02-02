package org.figuramc.fwl.gui.widgets.input.handlers;

import org.figuramc.fwl.gui.widgets.input.TextInput;

public abstract class ValueInputHandler<T>  {
    public abstract void onValue(T value);
}
