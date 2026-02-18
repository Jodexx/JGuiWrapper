package com.jodexindustries.jguiwrapper.paper.api.registry;

import com.jodexindustries.jguiwrapper.paper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.paper.api.gui.handler.item.ItemHandler;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.AdvancedGui;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings({"unused"})
public interface GlobalRegistry {

    @NotNull
    DataRegistry register(@NotNull String namespace);

    void registerLoader(@NotNull Key key, GuiDataLoader<AdvancedGui> loader);

    void registerHandler(@NotNull Key key, ItemHandler<?> handler);

    void unregister(@NotNull String namespace);

    Optional<GuiDataLoader<AdvancedGui>> getLoader(@NotNull Key key);

    Optional<DataRegistry> getRegistry(@NotNull String namespace);

    @UnmodifiableView
    @NotNull
    Collection<DataRegistry> getRegistries();
}
