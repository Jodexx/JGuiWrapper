package com.jodexindustries.jguiwrapper.api.gui.event;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GuiDragEvent extends CancellableGuiEvent {

    private final Set<Integer> rawSlots;

    public GuiDragEvent(@NotNull Object handle, @NotNull Gui gui, @NotNull User user, @NotNull Set<Integer> rawSlots) {
        super(handle, gui, user);

        this.rawSlots = rawSlots;
    }

    @NotNull
    public Set<Integer> rawSlots() {
        return rawSlots;
    }
}
