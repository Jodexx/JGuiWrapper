package com.jodexindustries.jguiwrapper.api.gui.handler;

import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public final class CancellableHandler <T extends InventoryEvent> implements InventoryHandler<T> {

    private final InventoryHandler<T> handler;
    private final boolean cancel;

    public CancellableHandler(@NotNull InventoryHandler<T> handler, boolean cancel) {
        this.handler = handler;
        this.cancel = cancel;
    }

    @Override
    public void handle(T event) {
        if(event instanceof Cancellable cancellable) {
            cancellable.setCancelled(cancel);
        }

        handler.handle(event);
    }

    public static <T extends InventoryEvent> InventoryHandler<T> wrap(@NotNull InventoryHandler<T> handler, boolean cancel) {
        return new CancellableHandler<>(handler, cancel);
    }
}
