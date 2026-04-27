package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.api.gui.event.GuiEvent;
import org.jetbrains.annotations.NotNull;

public interface GuiHandler<T extends GuiEvent, G extends Gui> {

    void handle(@NotNull T event, @NotNull G gui);
}
