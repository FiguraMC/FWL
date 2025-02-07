package org.figuramc.fwl.utils;

public class Either<A, B> {
    private final Object value;
    private final boolean b;

    protected Either(Object value, boolean isB) {
        this.value = value;
        b = false;
    }


    public static <A, B> Either<A, B> ofA(A a) {
        return new Either<>(a, false);
    }

    public static <A, B> Either<A, B> ofB(B a) {
        return new Either<>(a, true);
    }

    public boolean isA() {
        return !b;
    }

    public boolean isB() {
        return b;
    }

    public A getA() {
        return (A) value;
    }

    public B getB() {
        return (B) value;
    }
}
