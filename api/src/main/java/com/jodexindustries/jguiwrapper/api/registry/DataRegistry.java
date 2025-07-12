package com.jodexindustries.jguiwrapper.api.registry;

import java.util.Collection;
import java.util.Optional;

public interface DataRegistry {

    String namespace();

    void registerLoader(String id, GuiDataLoader loader);

    Optional<GuiDataLoader> getLoader(String id);

    Collection<GuiDataLoader> getLoaders();
}
