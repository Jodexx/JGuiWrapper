package com.jodexindustries.jguiwrapper.common.registry;

import com.jodexindustries.jguiwrapper.api.registry.DataRegistry;

public class DataRegistryImpl implements DataRegistry {

    private final String namespace;

    public DataRegistryImpl(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String namespace() {
        return namespace;
    }
}
