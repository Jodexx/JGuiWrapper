package com.jodexindustries.jguiwrapper.api.gui.event;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

public class GuiCloseEvent<T extends Gui> extends GuiEvent<T> {

    public GuiCloseEvent(@NotNull Object handle, @NotNull T gui, @NotNull User user) {
        super(handle, gui, user);
    }
}
