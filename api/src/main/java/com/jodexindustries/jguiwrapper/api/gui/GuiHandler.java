package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.api.gui.event.GuiEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public interface GuiHandler<T extends GuiEvent<G>, G extends Gui> {

    void handle(@NotNull T event, @NotNull G gui);
}
