package com.jodexindustries.jguiwrapper.api.gui.factory;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface GuiCreator<T extends Gui> {

    @NotNull
    T create(@NotNull GuiOptions options);
}
