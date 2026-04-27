package com.jodexindustries.jguiwrapper.api.gui.event;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

public abstract class GuiEvent<T extends Gui> {

    private final Object handle;
    private final T gui;
    private final User user;

    protected GuiEvent(@NotNull Object handle, @NotNull final T gui, @NotNull final User user) {
        this.handle = handle;
        this.gui = gui;
        this.user = user;
    }

    @NotNull
    public T gui() {
        return gui;
    }

    @NotNull
    public User user() {
        return user;
    }

    @NotNull
    public <E> E as(@NotNull Class<E> type) {
        if (type.isInstance(handle)) {
            return type.cast(handle);
        }
        throw new IllegalStateException("Unsupported handle type: " + type.getName());
    }
}
