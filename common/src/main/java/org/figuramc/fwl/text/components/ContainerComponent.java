package org.figuramc.fwl.text.components;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.providers.headless.HeadlessStyleProvider;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ContainerComponent extends AbstractComponent {
    private final ArrayList<AbstractComponent> siblings = new ArrayList<>();

    public ContainerComponent() {
    }

    public ContainerComponent(FWLStyle style) {
        super(style);
    }

    public ContainerComponent(HeadlessStyleProvider provider) {
        super(provider);
    }

    @Override
    public Iterator<AbstractComponent> siblings() {
        return siblings.iterator();
    }

    public ContainerComponent append(AbstractComponent other) {
        siblings.add(other);
        return this;
    }
}
