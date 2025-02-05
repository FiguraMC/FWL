package org.figuramc.fwl.text.components;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ContainerComponent extends AbstractComponent {
    private final ArrayList<AbstractComponent> siblings = new ArrayList<>();

    @Override
    public Iterator<AbstractComponent> siblings() {
        return siblings.iterator();
    }

    public ContainerComponent append(AbstractComponent other) {
        siblings.add(other);
        return this;
    }
}
