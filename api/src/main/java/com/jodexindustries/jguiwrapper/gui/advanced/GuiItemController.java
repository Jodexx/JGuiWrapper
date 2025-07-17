package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.api.registry.ItemHandler;
import com.jodexindustries.jguiwrapper.api.tools.Pair;
import net.kyori.adventure.key.Key;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Controller for managing items and their slots in an advanced GUI.
 * Supports both default items for all slots and slot-specific items.
 */
public class GuiItemController {

    private final AdvancedGui gui;

    private ItemWrapper defaultItemWrapper;
    private AdvancedGuiClickHandler defaultClickHandler;

    private final Map<Integer, ItemWrapper> slotSpecificItems = new HashMap<>();
    private final Map<Integer, AdvancedGuiClickHandler> slotClickHandlers = new HashMap<>();

    private final Set<Integer> slots = new LinkedHashSet<>();
    private final Set<Integer> oldSlots = new LinkedHashSet<>();

    private Pair<ItemHandler<?>, Class<?>> itemHandler;

    /**
     * Creates a new controller with specified slots
     * @param gui The parent GUI
     * @param defaultItemWrapper Default item for all slots
     * @param defaultClickHandler Click handler for all slots
     * @param slots Collection of slot indexes
     */
    public GuiItemController(@NotNull AdvancedGui gui, @Nullable ItemWrapper defaultItemWrapper,
                             AdvancedGuiClickHandler defaultClickHandler, @NotNull Collection<Integer> slots) {
        this.gui = gui;
        this.defaultItemWrapper = defaultItemWrapper;
        this.defaultClickHandler = defaultClickHandler;
        setSlots(slots);
    }

    /**
     * Creates a new controller with specified slots
     * @param gui The parent GUI
     * @param defaultItemWrapper Default item for all slots
     * @param defaultClickHandler Click handler for all slots
     * @param slots Array of slot indexes
     */
    public GuiItemController(@NotNull AdvancedGui gui, @Nullable ItemWrapper defaultItemWrapper,
                             AdvancedGuiClickHandler defaultClickHandler, int @NotNull ... slots) {
        this(gui, defaultItemWrapper, defaultClickHandler, Arrays.stream(slots).boxed().collect(Collectors.toList()));
    }

    public GuiItemController(@NotNull AdvancedGui gui, Key itemHandlerKey) {
        this(gui);

        itemHandler(itemHandlerKey);
    }

    /**
     * Creates an empty controller for specified GUI.
     * @param gui The parent GUI to associate with this controller
     */
    public GuiItemController(@NotNull AdvancedGui gui) {
        this(gui, null, null, Collections.emptySet());
    }

    /**
     * Adds a slot to be managed by this controller
     * @param slot Slot index to add (0-53)
     * @throws IndexOutOfBoundsException If slot is out of bounds
     */
    public void addSlot(Integer slot) {
        if (slot < 0 || slot >= 54)
            throw new IndexOutOfBoundsException("Slot " + slot + " is out of inventory bounds");
        slots.add(slot);
        redraw();
    }

    /**
     * Removes a slot from this controller
     * @param slot Slot index to remove
     */
    public void removeSlot(Integer slot) {
        if (slots.remove(slot)) {
            slotSpecificItems.remove(slot);
            slotClickHandlers.remove(slot);
            gui.removeClickHandlers(slot);
            gui.holder().getInventory().clear(slot);
        }
    }

    /**
     * Sets slots to be managed by this controller
     * @param slots Array of slot indexes
     */
    public void setSlots(int... slots) {
        setSlots(Arrays.stream(slots).boxed().collect(Collectors.toList()));
    }

    /**
     * Sets slots to be managed by this controller
     * @param newSlots Collection of slot indexes
     * @throws IndexOutOfBoundsException If any slot is out of bounds
     */
    public void setSlots(@NotNull Collection<Integer> newSlots) {
        for (int slot : newSlots) {
            if (slot < 0 || slot >= 54)
                throw new IndexOutOfBoundsException("Slot " + slot + " is out of inventory bounds");
        }

        oldSlots.clear();
        oldSlots.addAll(slots);
        slots.clear();
        slots.addAll(newSlots);
        redraw();
    }

    /**
     * Checks if a slot has an item assigned
     * @param slot Slot index to check
     * @return true if slot is managed and has an item
     */
    public boolean hasItem(int slot) {
        return slots.contains(slot) && getItemWrapper(slot) != null;
    }

    /**
     * Checks if controller has no items assigned
     * @return true if no default item and no slot-specific items
     */
    public boolean isEmpty() {
        return defaultItemWrapper == null && slotSpecificItems.isEmpty();
    }

    /**
     * Clears all items from managed slots (without removing slots)
     */
    public void clear() {
        for (int slot : slots) {
            gui.holder().getInventory().clear(slot);
        }
    }

    /**
     * Sets a specific item for a slot
     * @param slot Slot index
     * @param itemWrapper Item to set
     * @throws IllegalArgumentException If slot is not managed by this controller
     */
    public void setItemWrapper(int slot, @NotNull ItemWrapper itemWrapper) {
        if (!slots.contains(slot)) {
            throw new IllegalArgumentException("Slot " + slot + " is not managed by this controller");
        }
        slotSpecificItems.put(slot, itemWrapper);
        redraw(slot);
    }

    /**
     * Removes slot-specific item (reverts to default item)
     * @param slot Slot index
     */
    public void removeItemWrapper(int slot) {
        slotSpecificItems.remove(slot);
        redraw(slot);
    }

    /**
     * Updates items in specific slots
     * @param updater Consumer to modify items
     * @param slots Slot indexes to update
     */
    public void updateItemWrappers(@NotNull Consumer<ItemWrapper> updater, int... slots) {
        for (int slot : slots) {
            if (this.slots.contains(slot)) {
                updateItemWrapper(slot, updater);
            }
        }
    }

    /**
     * Sets the default item for all slots
     * @param itemWrapper Default item to set
     */
    public void defaultItemWrapper(@NotNull ItemWrapper itemWrapper) {
        this.defaultItemWrapper = itemWrapper;
        redraw();
    }

    /**
     * Gets the item for a specific slot
     * @param slot Slot index
     * @return ItemWrapper for the slot or default item if not specified
     */
    public @Nullable ItemWrapper getItemWrapper(int slot) {
        return slotSpecificItems.getOrDefault(slot, defaultItemWrapper);
    }

    /**
     * Gets the default item
     * @return Current default ItemWrapper
     */
    public @NotNull ItemWrapper defaultItemWrapper() {
        return defaultItemWrapper;
    }

    /**
     * Sets click handler for a specific slot
     * @param slot Slot index
     * @param clickHandler Click handler to set
     * @throws IllegalArgumentException If slot is not managed by this controller
     */
    public void setClickHandler(int slot, AdvancedGuiClickHandler clickHandler) {
        if (!slots.contains(slot)) {
            throw new IllegalArgumentException("Slot " + slot + " is not managed by this controller");
        }

        if (clickHandler != null) {
            slotClickHandlers.put(slot, clickHandler);
        } else {
            slotClickHandlers.remove(slot);
        }
        redraw(slot);
    }

    /**
     * Gets click handler for a specific slot
     * @param slot Slot index
     * @return Click handler for the slot or default handler if not specified
     */
    public @Nullable InventoryHandler<InventoryClickEvent> getClickHandler(int slot) {
        return slotClickHandlers.getOrDefault(slot, defaultClickHandler);
    }

    /**
     * Sets click handler for all slots
     * @param defaultClickHandler New click handler
     */
    public void defaultClickHandler(AdvancedGuiClickHandler defaultClickHandler) {
        this.defaultClickHandler = defaultClickHandler;
    }

    /**
     * Removes click handler for a specific slot
     * @param slot Slot index
     */
    public void removeClickHandler(int slot) {
        slotClickHandlers.remove(slot);
        redraw(slot);
    }

    /**
     * Gets all managed slots
     * @return Unmodifiable set of slot indexes
     */
    public @NotNull Set<Integer> slots() {
        return Collections.unmodifiableSet(slots);
    }

    public @NotNull AdvancedGui gui() {
        return gui;
    }

    /**
     * Gets current click handler
     * @return The click handler or null if not set
     */
    public @Nullable InventoryHandler<InventoryClickEvent> defaultClickHandler() {
        return defaultClickHandler;
    }

    /**
     * Redraws all managed slots
     */
    public void redraw() {
        for (int oldSlot : oldSlots) {
            gui.removeClickHandlers(oldSlot);
        }
        oldSlots.clear();
        oldSlots.addAll(slots);

        for (int slot : slots) {
            redraw(slot);
        }
    }

    private void redraw(int slot) {
        InventoryHandler<InventoryClickEvent> handler = getClickHandler(slot);
        if (handler != null) {
            gui.setClickHandlers(handler, slot);
        } else {
            gui.removeClickHandlers(slot);
        }

        ItemWrapper item = getItemWrapper(slot);
        if (item != null) {
            gui.holder().getInventory().setItem(slot, item.itemStack());
        }
    }

    /**
     * Updates all items (default and slot-specific)
     * @param updater Consumer to modify items
     */
    public void updateItemWrappers(@NotNull Consumer<ItemWrapper> updater) {
        if (defaultItemWrapper != null) {
            updater.accept(defaultItemWrapper);
            if (!defaultItemWrapper.isUpdated()) defaultItemWrapper.update();
        }

        for (ItemWrapper item : slotSpecificItems.values()) {
            updater.accept(item);
            if (!item.isUpdated()) item.update();
        }

        redraw();
    }

    /**
     * Updates item in a specific slot
     * @param slot Slot index
     * @param updater Consumer to modify the item
     */
    public void updateItemWrapper(int slot, @NotNull Consumer<ItemWrapper> updater) {
        ItemWrapper item = getItemWrapper(slot);
        if (item != null) {
            updater.accept(item);
            if (!item.isUpdated()) item.update();
            redraw(slot);
        }
    }

    public void itemHandler(Key itemHandlerKey) {
        if (itemHandlerKey == null) return;

        GuiApi.get().getRegistry().getRegistry(itemHandlerKey.namespace())
                .flatMap(dataRegistry -> dataRegistry.getHandler(itemHandlerKey.value()))
                .ifPresent(pair -> this.itemHandler = pair);
    }

    public void loadItemHandler() {
        if (this.itemHandler == null) return;

        Class<?> b = this.itemHandler.b();

        gui.getLoader(b).ifPresent(loader -> {
            if (b.isInstance(loader)) this.itemHandler.a().load(loader, this);
        });
    }

    public static class Builder {
        private final AdvancedGui gui;
        private ItemWrapper defaultItemWrapper;
        private AdvancedGuiClickHandler defaultClickHandler;
        private final Set<Integer> slots = new LinkedHashSet<>();
        private final Map<Integer, ItemWrapper> slotSpecificItems = new HashMap<>();
        private final Map<Integer, AdvancedGuiClickHandler> slotClickHandlers = new HashMap<>();
        private Key itemHandlerKey;

        /**
         * Creates a new builder for specified GUI.
         * @param gui The parent GUI to associate with controller
         */
        public Builder(@NotNull AdvancedGui gui) {
            this.gui = gui;
        }

        /**
         * Sets default item for all slots.
         * @param itemWrapper Default item to set
         * @return this builder for chaining
         */
        public Builder defaultItem(@Nullable ItemWrapper itemWrapper) {
            this.defaultItemWrapper = itemWrapper;
            return this;
        }

        /**
         * Sets default click handler for all slots.
         * @param clickHandler Click handler to set
         * @return this builder for chaining
         */
        public Builder defaultClickHandler(AdvancedGuiClickHandler clickHandler) {
            this.defaultClickHandler = clickHandler;
            return this;
        }

        /**
         * Adds slots to be managed by controller.
         * @param slots Array of slot indexes
         * @return this builder for chaining
         */
        public Builder slots(int... slots) {
            Arrays.stream(slots).forEach(this.slots::add);
            return this;
        }

        /**
         * Adds slots to be managed by controller.
         * @param slots Collection of slot indexes
         * @return this builder for chaining
         */
        public Builder slots(@NotNull Collection<Integer> slots) {
            this.slots.addAll(slots);
            return this;
        }

        /**
         * Sets specific item for slot.
         * @param slot Slot index
         * @param itemWrapper Item to set
         * @return this builder for chaining
         */
        public Builder slotItem(int slot, @NotNull ItemWrapper itemWrapper) {
            this.slotSpecificItems.put(slot, itemWrapper);
            this.slots.add(slot);
            return this;
        }

        /**
         * Sets specific click handler for slot.
         * @param slot Slot index
         * @param clickHandler Click handler to set
         * @return this builder for chaining
         */
        public Builder slotClickHandler(int slot, @Nullable AdvancedGuiClickHandler clickHandler) {
            this.slotClickHandlers.put(slot, clickHandler);
            this.slots.add(slot);
            return this;
        }

        public Builder itemHandler(Key itemHandlerKey) {
            this.itemHandlerKey = itemHandlerKey;
            return this;
        }

        /**
         * Builds and returns configured {@link GuiItemController}.
         * @return Configured controller instance
         */
        public GuiItemController build() {
            GuiItemController controller = new GuiItemController(gui, defaultItemWrapper, defaultClickHandler, slots);
            slotSpecificItems.forEach(controller::setItemWrapper);
            slotClickHandlers.forEach(controller::setClickHandler);

            controller.itemHandler(itemHandlerKey);

            return controller;
        }
    }

}