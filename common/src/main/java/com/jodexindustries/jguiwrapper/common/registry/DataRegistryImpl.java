package com.jodexindustries.jguiwrapper.common.registry;

import com.jodexindustries.jguiwrapper.api.registry.DataRegistry;
import com.jodexindustries.jguiwrapper.api.registry.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.registry.ItemHandler;
import com.jodexindustries.jguiwrapper.api.tools.Pair;
import com.jodexindustries.jguiwrapper.common.utils.ReflectionUtils;

import java.util.*;

public class DataRegistryImpl implements DataRegistry {

    private static final Map<String, GuiDataLoader> LOADERS = new HashMap<>();
    private static final Map<String, Pair<ItemHandler<?>, Class<?>>> HANDLERS = new HashMap<>();

    private final String namespace;

    public DataRegistryImpl(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String namespace() {
        return namespace;
    }

    @Override
    public void registerLoader(String id, GuiDataLoader loader) {
        if (LOADERS.containsKey(id)) {
            throw new IllegalStateException("GuiDataLoader already registered by id: " + id);
        }

        LOADERS.put(id, loader);
    }

    @Override
    public void registerHandler(String id, ItemHandler<?> handler) {
        if (HANDLERS.containsKey(id)) {
            throw new IllegalStateException("ItemHandler already registered by id: " + id);
        }

        Pair<ItemHandler<?>, Class<?>> pair = new Pair<>(handler, ReflectionUtils.getGenericClass(handler.getClass(), 0));

        HANDLERS.put(id, pair);
    }

    @Override
    public Optional<GuiDataLoader> getLoader(String id) {
        return Optional.ofNullable(LOADERS.get(id));
    }

    @Override
    public Optional<Pair<ItemHandler<?>, Class<?>>> getHandler(String id) {
        return Optional.ofNullable(HANDLERS.get(id));
    }

    @Override
    public Collection<GuiDataLoader> getLoaders() {
        return Collections.unmodifiableCollection(LOADERS.values());
    }

    @Override
    public Collection<Pair<ItemHandler<?>, Class<?>>> getHandlers() {
        return Collections.unmodifiableCollection(HANDLERS.values());
    }
}
