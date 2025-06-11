package com.jodexindustries.jguiwrapper.api.gui.handler;

import org.bukkit.event.inventory.InventoryEvent;

@FunctionalInterface
public interface InventoryHandler<T extends InventoryEvent> {

    void handle(T event);
}
