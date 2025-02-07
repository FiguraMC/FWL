package org.figuramc.fwl.text.properties;

public class AlternatingProperty extends NonVaryingProperty<Boolean> {
    public AlternatingProperty(boolean firstActive) {
        super(firstActive ? (i, p) -> i % 2 == 0 : (i, p) -> i % 2 == 1);
    }
}
