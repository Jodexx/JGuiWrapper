package com.jodexindustries.jguiwrapper.api.gui.types.advanced;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

public interface GuiDataLoader<G extends Gui> {

    void load(@NotNull G gui, @NotNull User user);
}

