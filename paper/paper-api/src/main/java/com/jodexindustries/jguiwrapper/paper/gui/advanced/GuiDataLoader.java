package com.jodexindustries.jguiwrapper.paper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public interface GuiDataLoader {

    void load(@NotNull AdvancedGui gui, @NotNull User user);
}
