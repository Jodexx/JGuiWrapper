package com.jodexindustries.jguiwrapper.common.registry;

import com.jodexindustries.jguiwrapper.api.registry.DataRegistry;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.api.registry.GuiDataLoader;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalRegistryImpl implements GlobalRegistry {

    private static final Map<String, DataRegistry> REGISTRIES = new ConcurrentHashMap<>();

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
    public void registerLoader(@NotNull Key key, GuiDataLoader loader) {
        getRegistry(key.namespace()).ifPresentOrElse(dataRegistry -> {
            dataRegistry.registerLoader(key.value(), loader);
        }, () -> {
            DataRegistry dataRegistry = register(key.namespace());
            dataRegistry.registerLoader(key.value(), loader);
        });
    }

    @Override
    public void unregister(@NotNull String namespace) {
        REGISTRIES.remove(namespace);
    }

    @Override
    public Optional<GuiDataLoader> getLoader(@NotNull Key key) {
        Optional<DataRegistry> registry = getRegistry(key.namespace());
        return registry.flatMap(dataRegistry -> dataRegistry.getLoader(key.value()));
    }

    @Override
    public Optional<DataRegistry> getRegistry(@NotNull String namespace) {
        return Optional.ofNullable(REGISTRIES.get(namespace));
    }

    @Override
    public @NotNull Collection<DataRegistry> getRegistries() {
        return Collections.unmodifiableCollection(REGISTRIES.values());
    }
}
