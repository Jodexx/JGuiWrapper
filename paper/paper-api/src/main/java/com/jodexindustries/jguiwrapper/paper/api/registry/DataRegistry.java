package com.jodexindustries.jguiwrapper.paper.api.registry;

import com.jodexindustries.jguiwrapper.paper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.paper.api.gui.handler.item.ItemHandler;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.utils.Pair;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings({"unused"})
public interface DataRegistry {

    String namespace();

    void registerLoader(String id, GuiDataLoader<AdvancedGui> loader);

    void registerHandler(String id, ItemHandler<?> handler);

    Optional<GuiDataLoader<AdvancedGui>> getLoader(String id);

    Optional<Pair<ItemHandler<?>, Class<?>>> getHandler(String id);

    @UnmodifiableView
    Collection<GuiDataLoader<AdvancedGui>> getLoaders();

    @UnmodifiableView
    Collection<Pair<ItemHandler<?>, Class<?>>> getHandlers();
}
