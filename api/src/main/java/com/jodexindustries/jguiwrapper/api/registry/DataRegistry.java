package com.jodexindustries.jguiwrapper.api.registry;

import com.jodexindustries.jguiwrapper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.gui.handler.item.ItemHandler;
import com.jodexindustries.jguiwrapper.api.tools.Pair;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings({"unused"})
public interface DataRegistry {

    String namespace();

    void registerLoader(String id, GuiDataLoader loader);

    void registerHandler(String id, ItemHandler<?> handler);

    Optional<GuiDataLoader> getLoader(String id);

    Optional<Pair<ItemHandler<?>, Class<?>>> getHandler(String id);

    Collection<GuiDataLoader> getLoaders();

    Collection<Pair<ItemHandler<?>, Class<?>>> getHandlers();
}
