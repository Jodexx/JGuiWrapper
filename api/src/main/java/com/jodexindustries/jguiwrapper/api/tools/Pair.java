package com.jodexindustries.jguiwrapper.api.tools;

public class Pair<A, B> {

    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Pair<A, B> empty() {
        return new Pair<>(null, null);
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }
}
