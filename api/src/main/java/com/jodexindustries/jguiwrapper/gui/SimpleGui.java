package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Represents a simple GUI implementation with slot-based click handlers and event consumers.
 * <p>
 * Provides methods to register handlers for slot clicks, open/close/drag events, and control slot cancellation behavior.
 * Designed for easy extension and use in plugin GUI development.
 */
public abstract class SimpleGui extends AbstractGui {

    private final Map<Integer, InventoryHandler<InventoryClickEvent>> slotClickHandlers = new HashMap<>();

    private Consumer<InventoryOpenEvent> openEventConsumer;
    private Consumer<InventoryCloseEvent> closeEventConsumer;
    private Consumer<InventoryDragEvent> dragEventConsumer;

    private boolean cancelEmptySlots = true;

    public SimpleGui(@NotNull String title) {
        super(title);
    }

    public SimpleGui(int size, @NotNull String title) {
        super(size, title);
    }

    public SimpleGui(@NotNull Component title) {
        super(title);
    }

    public SimpleGui(InventoryType type, @NotNull Component title) {
        super(type, title);
    }

    public SimpleGui(int size, @NotNull Component title) {
        super(size, title);
    }

    @Override
    public final void onOpen(@NotNull InventoryOpenEvent event) {
        if (openEventConsumer != null) openEventConsumer.accept(event);
    }

    @Override
    public final void onClose(@NotNull InventoryCloseEvent event) {
        if (closeEventConsumer != null) closeEventConsumer.accept(event);
    }

    @Override
    public final void onDrag(@NotNull InventoryDragEvent event) {
        if (dragEventConsumer != null) {
            dragEventConsumer.accept(event);
        } else {
            event.setCancelled(true);
        }
    }

    @Override
    public final void onClick(@NotNull InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
            return;
        }

        int slot = event.getRawSlot();

        if (slot >= size()) return;

        InventoryHandler<InventoryClickEvent> handler = slotClickHandlers.get(slot);

        if (handler != null) {
            handler.handle(event, this);
        } else if (cancelEmptySlots) {
            event.setCancelled(true);
        }
    }

    /**
     * Registers a consumer to be called when the GUI is opened.
     * @param consumer the consumer to handle InventoryOpenEvent
     */
    public final void onOpen(Consumer<InventoryOpenEvent> consumer) {
        this.openEventConsumer = consumer;
    }

    /**
     * Registers a consumer to be called when the GUI is closed.
     * @param consumer the consumer to handle InventoryCloseEvent
     */
    public final void onClose(Consumer<InventoryCloseEvent> consumer) {
        this.closeEventConsumer = consumer;
    }

    /**
     * Registers a consumer to be called when the GUI is dragged in.
     * @param consumer the consumer to handle InventoryDragEvent
     */
    public final void onDrag(Consumer<InventoryDragEvent> consumer) {
        this.dragEventConsumer = consumer;
    }

    public void setClickHandlers(@NotNull InventoryHandler<InventoryClickEvent> handler, int @NotNull ... slots) {
        if (slots.length == 0) {
            setClickHandlers0(handler, IntStream.range(0, super.size()).toArray());
            return;
        }

        setClickHandlers0(handler, slots);
    }

    private void setClickHandlers0(@NotNull InventoryHandler<InventoryClickEvent> handler, int @NotNull ... slots) {
        for (int slot : slots) {
            slotClickHandlers.put(slot, handler);
        }
    }

    public void removeClickHandlers(int @NotNull ... slots) {
        if (slots.length == 0) {
            slotClickHandlers.clear();
            return;
        }

        removeClickHandlers0(slots);
    }

    private void removeClickHandlers0(int @NotNull ... slots) {
        for (int slot : slots) {
            slotClickHandlers.remove(slot);
        }
    }

    public void setCancelEmptySlots(boolean cancelEmptySlots) {
        this.cancelEmptySlots = cancelEmptySlots;
    }
}