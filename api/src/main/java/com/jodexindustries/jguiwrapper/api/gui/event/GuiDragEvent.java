package com.jodexindustries.jguiwrapper.api.gui.event;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GuiDragEvent extends CancellableGuiEvent {

    private final Collection<Integer> rawSlots;

    public GuiDragEvent(@NotNull Object handle, @NotNull Gui gui, @NotNull User user, @NotNull Collection<Integer> rawSlots) {
        super(handle, gui, user);

        this.rawSlots = rawSlots;
    }

    @NotNull
    public Collection<Integer> rawSlots() {
        return rawSlots;
    }
}
