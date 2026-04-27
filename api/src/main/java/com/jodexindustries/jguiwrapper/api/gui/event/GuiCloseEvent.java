package com.jodexindustries.jguiwrapper.api.gui.event;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

public class GuiCloseEvent extends GuiEvent {

    public GuiCloseEvent(@NotNull Object handle, @NotNull Gui gui, @NotNull User user) {
        super(handle, gui, user);
    }
}
