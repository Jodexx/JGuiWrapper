package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Represents a simple GUI implementation with slot-based click handlers and event consumers.
 * <p>
 * Provides methods to register handlers for slot clicks, open/close/drag events, and control slot cancellation behavior.
 * Designed for easy extension and use in plugin GUI development.
 */
@SuppressWarnings({"unused"})
public abstract class SimpleGui extends AbstractGui {

    private final Map<Integer, InventoryHandler<InventoryClickEvent>> slotClickHandlers = new HashMap<>();

    private final List<Consumer<InventoryOpenEvent>> openEventConsumers = new ArrayList<>();
    private final List<Consumer<InventoryCloseEvent>> closeEventConsumers = new ArrayList<>();
    private final List<Consumer<InventoryDragEvent>> dragEventConsumers = new ArrayList<>();

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
        for (Consumer<InventoryOpenEvent> consumer : openEventConsumers) {
            consumer.accept(event);
        }
    }

    @Override
    public final void onClose(@NotNull InventoryCloseEvent event) {
        for (Consumer<InventoryCloseEvent> consumer : closeEventConsumers) {
            consumer.accept(event);
        }
    }

    @Override
    public final void onDrag(@NotNull InventoryDragEvent event) {
        if (cancelEmptySlots) {
            for (Integer rawSlot : event.getRawSlots()) {
                if (rawSlot < size()) {
                    event.setCancelled(true);
                }
            }
        }

        for (Consumer<InventoryDragEvent> consumer : dragEventConsumers) {
            consumer.accept(event);
        }
    }

    @Override
    public final void onClick(@NotNull InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER) {
                event.setCancelled(cancelEmptySlots);
            }
        }

        int slot = event.getRawSlot();

        // TODO Support for player inventory clicks, with disable option (improved handling)
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
     *
     * @param consumer the consumer to handle InventoryOpenEvent
     */
    public final void onOpen(Consumer<InventoryOpenEvent> consumer) {
        this.openEventConsumers.add(consumer);
    }

    /**
     * Registers a consumer to be called when the GUI is closed.
     *
     * @param consumer the consumer to handle InventoryCloseEvent
     */
    public final void onClose(Consumer<InventoryCloseEvent> consumer) {
        this.closeEventConsumers.add(consumer);
    }

    /**
     * Registers a consumer to be called when the GUI is dragged in.
     *
     * @param consumer the consumer to handle InventoryDragEvent
     */
    public final void onDrag(Consumer<InventoryDragEvent> consumer) {
        this.dragEventConsumers.add(consumer);
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
