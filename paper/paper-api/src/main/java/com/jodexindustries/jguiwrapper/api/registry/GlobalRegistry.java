package com.jodexindustries.jguiwrapper.api.registry;

import com.jodexindustries.jguiwrapper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.gui.handler.item.ItemHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings({"unused"})
public interface GlobalRegistry {

    @NotNull
    DataRegistry register(@NotNull String namespace);

    void registerLoader(@NotNull Key key, GuiDataLoader loader);

    void registerHandler(@NotNull Key key, ItemHandler<?> handler);

    void unregister(@NotNull String namespace);

    Optional<GuiDataLoader> getLoader(@NotNull Key key);

    Optional<DataRegistry> getRegistry(@NotNull String namespace);

    @UnmodifiableView
    @NotNull
    Collection<DataRegistry> getRegistries();
}
