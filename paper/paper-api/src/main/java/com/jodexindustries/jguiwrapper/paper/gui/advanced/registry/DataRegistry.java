package com.jodexindustries.jguiwrapper.paper.gui.advanced.registry;

import com.jodexindustries.jguiwrapper.api.utils.Pair;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.GuiDataLoader;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.item.ItemHandler;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings({"unused"})
public interface DataRegistry {

    String namespace();

    void registerLoader(String id, GuiDataLoader loader);

    void registerHandler(String id, ItemHandler<?> handler);

    Optional<GuiDataLoader> getLoader(String id);

    Optional<Pair<ItemHandler<?>, Class<?>>> getHandler(String id);

    @UnmodifiableView
    Collection<GuiDataLoader> getLoaders();

    @UnmodifiableView
    Collection<Pair<ItemHandler<?>, Class<?>>> getHandlers();
}
