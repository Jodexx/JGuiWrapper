package com.jodexindustries.jguiwrapper.common.registry;

import com.jodexindustries.jguiwrapper.api.registry.DataRegistry;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GlobalRegistryImpl implements GlobalRegistry {

    private static final Map<String, DataRegistry> REGISTRIES = new HashMap<>();

    @Override
    public @NotNull DataRegistry register(@NotNull String namespace) {
        if (REGISTRIES.containsKey(namespace)) {
            throw new IllegalStateException("Namespace already registered: " + namespace);
        }

        DataRegistryImpl registry = new DataRegistryImpl(namespace);
        REGISTRIES.put(namespace, registry);
        return registry;
    }

    @Override
    public void unregister(@NotNull String namespace) {
        REGISTRIES.remove(namespace);
    }

    @Override
    public Optional<DataRegistry> getRegistry(@NotNull String namespace) {
        return Optional.of(REGISTRIES.get(namespace));
    }

    @Override
    public @NotNull Collection<DataRegistry> getRegistries() {
        return Collections.unmodifiableCollection(REGISTRIES.values());
    }
}
