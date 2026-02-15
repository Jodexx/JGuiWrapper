package com.jodexindustries.jguiwrapper.api.gui.handler;

import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import org.bukkit.event.inventory.InventoryEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public interface InventoryHandler<T extends InventoryEvent> {

    void handle(@NotNull T event, @NotNull SimpleGui gui);
}
