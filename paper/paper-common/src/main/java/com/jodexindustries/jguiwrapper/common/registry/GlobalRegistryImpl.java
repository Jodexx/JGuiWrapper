package com.jodexindustries.jguiwrapper.common.registry;

import com.jodexindustries.jguiwrapper.paper.api.registry.DataRegistry;
import com.jodexindustries.jguiwrapper.paper.api.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.paper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.paper.api.gui.handler.item.ItemHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
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
        Optional<DataRegistry> optionalRegistry = getRegistry(key.namespace());
        if (optionalRegistry.isPresent()) {
            optionalRegistry.get().registerLoader(key.value(), loader);
        } else {
            DataRegistry dataRegistry = register(key.namespace());
            dataRegistry.registerLoader(key.value(), loader);
        }
    }

    @Override
    public void registerHandler(@NotNull Key key, ItemHandler<?> handler) {
        Optional<DataRegistry> optionalRegistry = getRegistry(key.namespace());
        if (optionalRegistry.isPresent()) {
            optionalRegistry.get().registerHandler(key.value(), handler);
        } else {
            DataRegistry dataRegistry = register(key.namespace());
            dataRegistry.registerHandler(key.value(), handler);
        }
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
