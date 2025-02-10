package org.figuramc.fwl.gui.themes.configs;

import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.themes.FWLBreeze;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.utils.Pair;

public class FWLBreezeConfig extends FWLAbstractConfig<FWLBreeze> {
    private final Pair<AbstractComponent, FieldConstructor<FWLBreeze>>[] constructors;
    public FWLBreezeConfig(FWLBreeze theme, float width, float height, Pair<AbstractComponent, FieldConstructor<FWLBreeze>>[] constructors) {
        super(theme, width, height);
        this.constructors = constructors;
        init();
    }

    @Override
    protected Pair<AbstractComponent, FieldConstructor<FWLBreeze>>[] getConstructors() {
        return constructors;
    }
}
