package com.jodexindustries.jguiwrapper.api.gui.handler;

import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import org.bukkit.event.inventory.InventoryEvent;

public interface InventoryHandler<T extends InventoryEvent> {

    void handle(T event, SimpleGui gui);
}
