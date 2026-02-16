package com.jodexindustries.jguiwrapper.paper.gui;

import com.jodexindustries.jguiwrapper.paper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Constructs a GUI with the default size (54) and a string title.
     *
     * @param title The GUI title as a string
     */
    public SimpleGui(@NotNull String title) {
        super(title);
    }

    /**
     * Constructs a GUI with a specific size and string title.
     *
     * @param size  The inventory size
     * @param title The GUI title as a string
     */
    public SimpleGui(int size, @NotNull String title) {
        super(size, title);
    }

    /**
     * Constructs a GUI with the default CHEST type and a component title.
     *
     * @param title The GUI title as a Component
     */
    public SimpleGui(@NotNull Component title) {
        super(title);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title.
     *
     * @param type  The inventory type
     * @param title The GUI title as a Component
     */
    public SimpleGui(@NotNull InventoryType type, @NotNull Component title) {
        super(type, title);
    }

    /**
     * Constructs a GUI with a specific size, optional type, and component title.
     *
     * @param size  The inventory size
     * @param title The GUI title as a Component
     */
    public SimpleGui(int size, @NotNull Component title) {
        super(size, title);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title with serializer.
     *
     * @param type              The inventory type.
     * @param title             The GUI title as a {@link Component}.
     * @param defaultSerializer The default serializer used for converting between plain strings and {@link Component}
     *                          instances. If {@code null}, the
     *                          {@link #defaultSerializer} will be used.
     */
    public SimpleGui(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(type, title, defaultSerializer);
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
        final int slot = event.getRawSlot();

        // do not need to handle undefined slot (outside the window)
        if (slot < 0) return;

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            final Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER && cancelEmptySlots) {
                event.setCancelled(true);
            }
        }

        final InventoryHandler<InventoryClickEvent> handler = slotClickHandlers.get(slot);

        if (handler != null) {
            handler.handle(event, this);
        } else if (slot < size() && cancelEmptySlots) {
            // cancel for gui slots, do not touch player inventory
            event.setCancelled(true);
        }
    }

    /**
     * Registers a consumer to be called when the GUI is opened.
     *
     * @param consumer the consumer to handle InventoryOpenEvent
     */
    public final void onOpen(@NotNull Consumer<InventoryOpenEvent> consumer) {
        this.openEventConsumers.add(consumer);
    }

    /**
     * Registers a consumer to be called when the GUI is closed.
     *
     * @param consumer the consumer to handle InventoryCloseEvent
     */
    public final void onClose(@NotNull Consumer<InventoryCloseEvent> consumer) {
        this.closeEventConsumers.add(consumer);
    }

    /**
     * Registers a consumer to be called when the GUI is dragged in.
     *
     * @param consumer the consumer to handle InventoryDragEvent
     */
    public final void onDrag(@NotNull Consumer<InventoryDragEvent> consumer) {
        this.dragEventConsumers.add(consumer);
    }

    /**
     * Sets click handlers for specific slots or all slots if none are specified.
     *
     * @param handler the handler to manage slot clicks
     * @param slots   the slots to assign the handler to
     */
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

    /**
     * Removes click handlers from specific slots or all slots if none are specified.
     *
     * @param slots the slots to remove handlers from
     */
    public void removeClickHandlers(int @NotNull ... slots) {
        if (slots.length == 0) {
            slotClickHandlers.clear();
            return;
        }

        for (int slot : slots) {
            slotClickHandlers.remove(slot);
        }
    }

    /**
     * Sets whether empty slots should be canceled.
     * <p>
     * By "empty slots" we mean slots that have no assigned click handlers.
     *
     * @param cancelEmptySlots true to cancel empty slots, false otherwise
     */
    public void setCancelEmptySlots(boolean cancelEmptySlots) {
        this.cancelEmptySlots = cancelEmptySlots;
    }
}
