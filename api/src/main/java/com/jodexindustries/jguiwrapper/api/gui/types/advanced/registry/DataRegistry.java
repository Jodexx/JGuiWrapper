package com.jodexindustries.jguiwrapper.api.gui.types.advanced.registry;

import com.jodexindustries.jguiwrapper.api.gui.types.advanced.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.item.ItemHandler;
import com.jodexindustries.jguiwrapper.api.utils.Pair;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;

public interface DataRegistry {

    String namespace();

    void registerLoader(String id, GuiDataLoader<?> loader);

    void registerHandler(String id, ItemHandler<?> handler);

    Optional<GuiDataLoader<?>> getLoader(String id);

    Optional<Pair<ItemHandler<?>, Class<?>>> getHandler(String id);

    @UnmodifiableView
    Collection<GuiDataLoader<?>> getLoaders();

    @UnmodifiableView
    Collection<Pair<ItemHandler<?>, Class<?>>> getHandlers();
}

