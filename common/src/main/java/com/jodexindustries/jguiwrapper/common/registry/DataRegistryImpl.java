package com.jodexindustries.jguiwrapper.common.registry;

import com.jodexindustries.jguiwrapper.api.registry.DataRegistry;
import com.jodexindustries.jguiwrapper.api.registry.GuiDataLoader;

import java.util.*;

public class DataRegistryImpl implements DataRegistry {

    private static final Map<String, GuiDataLoader> LOADERS = new HashMap<>();

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
    public Optional<GuiDataLoader> getLoader(String id) {
        return Optional.ofNullable(LOADERS.get(id));
    }

    @Override
    public Collection<GuiDataLoader> getLoaders() {
        return Collections.unmodifiableCollection(LOADERS.values());
    }
}
