package org.figuramc.fwl.gui.themes.configs;

import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.themes.FWLBreeze;
import org.figuramc.fwl.utils.Pair;

public class FWLBreezeConfig extends FWLAbstractConfig<FWLBreeze> {
    private final Pair<Component, FieldConstructor<FWLBreeze>>[] constructors;
    public FWLBreezeConfig(FWLBreeze theme, float width, float height, Pair<Component, FieldConstructor<FWLBreeze>>[] constructors) {
        super(theme, width, height);
        this.constructors = constructors;
        init();
    }

    @Override
    protected Pair<Component, FieldConstructor<FWLBreeze>>[] getConstructors() {
        return constructors;
    }
}
