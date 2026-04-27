package com.jodexindustries.jguiwrapper.api.gui.factory;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface GuiFactory {

    @NotNull
    <T extends Gui> T create(@NotNull GuiType type, @NotNull GuiOptions options);

    @NotNull
    <T extends Gui> T create(@NotNull String key, @NotNull GuiOptions options);

    <T extends Gui> void register(@NotNull GuiType type, @NotNull GuiCreator<T> creator);

    <T extends Gui> void register(@NotNull String key, @NotNull GuiCreator<T> creator);

    boolean unregister(@NotNull GuiType type);

    boolean unregister(@NotNull String key);
}
