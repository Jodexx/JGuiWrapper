package com.jodexindustries.jguiwrapper.utils;

@SuppressWarnings({"unused"})
public record Pair<A, B>(A a, B b) {

    public static <A, B> Pair<A, B> empty() {
        return new Pair<>(null, null);
    }
}
