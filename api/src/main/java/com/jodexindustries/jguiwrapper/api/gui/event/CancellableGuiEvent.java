package com.jodexindustries.jguiwrapper.api.gui.event;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

public class CancellableGuiEvent extends GuiEvent {

    private boolean cancel;

    protected CancellableGuiEvent(@NotNull Object handle, @NotNull Gui gui, @NotNull User user) {
        super(handle, gui, user);
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancelled() {
        return cancel;
    }
}
