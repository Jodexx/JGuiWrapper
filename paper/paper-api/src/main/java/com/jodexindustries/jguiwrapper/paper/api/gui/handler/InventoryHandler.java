package com.jodexindustries.jguiwrapper.paper.api.gui.handler;

import com.jodexindustries.jguiwrapper.paper.gui.SimpleGui;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public interface InventoryHandler<T> {

    void handle(@NotNull T event, @NotNull SimpleGui gui);
}
