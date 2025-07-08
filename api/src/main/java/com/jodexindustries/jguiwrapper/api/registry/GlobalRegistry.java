package com.jodexindustries.jguiwrapper.api.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface GlobalRegistry {

    @NotNull
    DataRegistry register(@NotNull String namespace);

    void unregister(@NotNull String namespace);

    Optional<DataRegistry> getRegistry(@NotNull String namespace);

    @NotNull
    Collection<DataRegistry> getRegistries();
}
