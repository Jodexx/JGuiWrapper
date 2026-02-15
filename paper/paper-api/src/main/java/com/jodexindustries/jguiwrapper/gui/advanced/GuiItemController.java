package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.api.gui.handler.item.HandlerContext;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.api.gui.handler.item.ItemHandler;
import com.jodexindustries.jguiwrapper.api.tools.Pair;
import net.kyori.adventure.key.Key;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Controller for managing items and their slots in an advanced GUI.
 * Supports both default items for all slots and slot-specific items.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class GuiItemController {

    private final AdvancedGui gui;

    private ItemWrapper defaultItemWrapper;
    private AdvancedGuiClickHandler defaultClickHandler;

    private final Map<Integer, ItemWrapper> slotSpecificItems = new HashMap<>();
    private final Map<Integer, AdvancedGuiClickHandler> slotClickHandlers = new HashMap<>();

    private final Collection<Integer> defaultSlots;

    private final Set<Integer> slots = new LinkedHashSet<>();
    private final Set<Integer> oldSlots = new LinkedHashSet<>();

    private Pair<ItemHandler<?>, Class<?>> itemHandler;

    /**
     * Creates a new controller with specified slots
     *
     * @param gui                 The parent GUI
     * @param defaultItemWrapper  Default item for all slots
     * @param defaultClickHandler Click handler for all slots
     * @param slots               Collection of slot indexes
     */
    protected GuiItemController(@NotNull AdvancedGui gui, @Nullable ItemWrapper defaultItemWrapper,
                             AdvancedGuiClickHandler defaultClickHandler, @NotNull Collection<Integer> slots) {
        this.gui = gui;
        this.defaultItemWrapper = defaultItemWrapper;
        this.defaultClickHandler = defaultClickHandler;
        this.defaultSlots = slots;
    }

    /**
     * Creates a new controller with specified slots
     *
     * @param gui                 The parent GUI
     * @param defaultItemWrapper  Default item for all slots
     * @param defaultClickHandler Click handler for all slots
     * @param slots               Array of slot indexes
     */
    protected GuiItemController(@NotNull AdvancedGui gui, @Nullable ItemWrapper defaultItemWrapper,
                             AdvancedGuiClickHandler defaultClickHandler, int @NotNull ... slots) {
        this(gui, defaultItemWrapper, defaultClickHandler, Arrays.stream(slots).boxed().collect(Collectors.toList()));
    }

    protected GuiItemController(@NotNull AdvancedGui gui, Key itemHandlerKey) {
        this(gui);

        itemHandler(itemHandlerKey);
    }

    /**
     * Creates an empty controller for specified GUI.
     *
     * @param gui The parent GUI to associate with this controller
     */
    public GuiItemController(@NotNull AdvancedGui gui) {
        this(gui, null, null, Collections.emptySet());
    }

    /**
     * Adds a slot to be managed by this controller
     *
     * @param slot Slot index to add
     * @throws IndexOutOfBoundsException If slot is out of bounds
     */
    public void addSlot(@Range(from = 0, to = 53) int slot) {
        validateSlot(slot);
        slots.add(slot);
        redraw();
    }

    /**
     * Removes a slot from this controller
     *
     * @param slot Slot index to remove
     */
    public void removeSlot(@Range(from = 0, to = 53) int slot) {
        if (slots.remove(slot)) {
            slotSpecificItems.remove(slot);
            slotClickHandlers.remove(slot);
            clear(slot);
        }
    }

    protected void drawSlots() {
        setSlots(this.defaultSlots);
    }

    /**
     * Sets slots to be managed by this controller
     *
     * @param slots Array of slot indexes
     */
    public void setSlots(int... slots) {
        setSlots(Arrays.stream(slots).boxed().collect(Collectors.toList()));
    }

    /**
     * Sets slots to be managed by this controller
     *
     * @param newSlots Collection of slot indexes
     * @throws IndexOutOfBoundsException If any slot is out of bounds
     */
    public void setSlots(@NotNull Collection<Integer> newSlots) {
        for (int slot : newSlots) {
            validateSlot(slot);

            Optional<GuiItemController> existingController = gui.getController(slot);

            if (existingController.isPresent() && !existingController.get().equals(this)) {
                throw new IllegalArgumentException("Slot " + slot + " is already occupied by another controller");
            }
        }

        for (Integer oldSlot : oldSlots) {
            gui.slotMap.remove(oldSlot);
        }

        for (int slot : newSlots) {
            gui.slotMap.put(slot, this);
        }

        oldSlots.clear();
        oldSlots.addAll(slots);
        slots.clear();
        slots.addAll(newSlots);
        redraw();
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= gui.size()) {
            throw new IndexOutOfBoundsException("Slot " + slot + " is out of inventory bounds");
        }
    }

    /**
     * Checks if a slot has an item assigned
     *
     * @param slot Slot index to check
     * @return true if slot is managed and has an item
     */
    public boolean hasItem(int slot) {
        return slots.contains(slot) && getItem(slot) != null;
    }

    /**
     * Checks if controller has no items assigned
     *
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
            clear(slot);
        }
    }

    public void clear(int slot) {
        gui.removeClickHandlers(slot);
        gui.holder().getInventory().clear(slot);
    }

    /**
     * Sets a specific item for a slot
     *
     * @param slot        Slot index
     * @param itemWrapper Item to set
     * @throws IllegalArgumentException If slot is not managed by this controller
     */
    public void setItem(int slot, @NotNull ItemWrapper itemWrapper) {
        if (!slots.contains(slot)) {
            throw new IllegalArgumentException("Slot " + slot + " is not managed by this controller");
        }
        slotSpecificItems.put(slot, itemWrapper);
        redraw(slot);
    }

    /**
     * Removes slot-specific item (reverts to default item)
     *
     * @param slot Slot index
     */
    public void removeItem(int slot) {
        slotSpecificItems.remove(slot);
        redraw(slot);
    }

    /**
     * Updates items in specific slots with player
     *
     * @param updater Consumer to modify items
     * @param player  Player to placeholder update
     * @param slots   Slot indexes to update
     */
    public void updateItems(@NotNull Consumer<ItemWrapper> updater, @Nullable OfflinePlayer player, int... slots) {
        for (int slot : slots) {
            if (this.slots.contains(slot)) {
                updateItem(slot, updater, player);
            }
        }
    }

    /**
     * Updates items in specific slots
     *
     * @param updater Consumer to modify items
     * @param slots   Slot indexes to update
     */
    public void updateItems(@NotNull Consumer<ItemWrapper> updater, int... slots) {
        for (int slot : slots) {
            if (this.slots.contains(slot)) {
                updateItem(slot, updater);
            }
        }
    }

    /**
     * Sets the default item for all slots
     *
     * @param itemWrapper Default item to set
     */
    public void defaultItem(@NotNull ItemWrapper itemWrapper) {
        this.defaultItemWrapper = itemWrapper;
        redraw();
    }

    /**
     * Gets the item for a specific slot
     *
     * @param slot Slot index
     * @return ItemWrapper for the slot or default item if not specified
     */
    public @Nullable ItemWrapper getItem(int slot) {
        return slotSpecificItems.getOrDefault(slot, defaultItemWrapper);
    }

    /**
     * Gets the default item
     *
     * @return Current default ItemWrapper
     */
    public @NotNull ItemWrapper defaultItem() {
        return defaultItemWrapper;
    }

    /**
     * Sets click handler for a specific slot
     *
     * @param slot         Slot index
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
     *
     * @param slot Slot index
     * @return Click handler for the slot or default handler if not specified
     */
    public @Nullable InventoryHandler<InventoryClickEvent> getClickHandler(int slot) {
        return slotClickHandlers.getOrDefault(slot, defaultClickHandler);
    }

    /**
     * Sets click handler for all slots
     *
     * @param defaultClickHandler New click handler
     */
    public void defaultClickHandler(AdvancedGuiClickHandler defaultClickHandler) {
        this.defaultClickHandler = defaultClickHandler;
    }

    /**
     * Removes click handler for a specific slot
     *
     * @param slot Slot index
     */
    public void removeClickHandler(int slot) {
        slotClickHandlers.remove(slot);
        redraw(slot);
    }

    /**
     * Gets all managed slots
     *
     * @return Unmodifiable set of slot indexes
     */
    @UnmodifiableView
    public @NotNull Set<Integer> slots() {
        return Collections.unmodifiableSet(slots);
    }

    public @NotNull AdvancedGui gui() {
        return gui;
    }

    /**
     * Gets current click handler
     *
     * @return The click handler or null if not set
     */
    public @Nullable InventoryHandler<InventoryClickEvent> defaultClickHandler() {
        return defaultClickHandler;
    }

    /**
     * Redraws all managed slots
     */
    public void redraw() {
        if (!oldSlots.isEmpty()) {
            for (int oldSlot : oldSlots) {
                clear(oldSlot);
            }
            oldSlots.clear();
        }

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

        ItemWrapper item = getItem(slot);
        if (item != null) {
            gui.holder().getInventory().setItem(slot, item.itemStack());
        }
    }

    /**
     * Updates all items (default and slot-specific) with player
     *
     * @param player  Player to placeholder update
     * @param updater Consumer to modify items
     */
    public void updateItems(@NotNull Consumer<ItemWrapper> updater, @Nullable OfflinePlayer player) {
        if (defaultItemWrapper != null) {
            updater.accept(defaultItemWrapper);
            if (!defaultItemWrapper.isUpdated()) defaultItemWrapper.update(player);
        }

        for (ItemWrapper item : slotSpecificItems.values()) {
            updater.accept(item);
            if (!item.isUpdated()) item.update(player);
        }

        redraw();
    }

    /**
     * Updates all items (default and slot-specific)
     *
     * @param updater Consumer to modify items
     */
    public void updateItems(@NotNull Consumer<ItemWrapper> updater) {
        updateItems(updater, (OfflinePlayer) null);
    }

    /**
     * Updates item in a specific slot
     *
     * @param slot    Slot index
     * @param updater Consumer to modify the item
     */
    public void updateItem(int slot, @NotNull Consumer<ItemWrapper> updater) {
        updateItem(slot, updater, null);
    }

    /**
     * Updates item in a specific slot
     *
     * @param slot    Slot index
     * @param updater Consumer to modify the item
     */
    public void updateItem(int slot, @NotNull Consumer<ItemWrapper> updater, @Nullable OfflinePlayer player) {
        ItemWrapper item = getItem(slot);
        if (item != null) {
            updater.accept(item);
            if (!item.isUpdated()) item.update(player);
            redraw(slot);
        }
    }

    public void itemHandler(Key itemHandlerKey) {
        if (itemHandlerKey == null) return;

        GuiApi.get().getRegistry().getRegistry(itemHandlerKey.namespace())
                .flatMap(dataRegistry -> dataRegistry.getHandler(itemHandlerKey.value()))
                .ifPresent(pair -> this.itemHandler = pair);
    }

    public void loadItemHandler(@NotNull LoadType loadType) {
        loadItemHandler(loadType, null);
    }

    public void loadItemHandler(@NotNull LoadType loadType, @Nullable HumanEntity player) {
        if (this.itemHandler == null) return;

        Class<?> b = this.itemHandler.b();

        gui.getLoader(b).ifPresent(loader -> {
            if (b.isInstance(loader)) this.itemHandler.a().load(loader, this, new HandlerContext(loadType, player));
        });
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GuiItemController that = (GuiItemController) o;
        return Objects.equals(gui, that.gui) && Objects.equals(defaultItemWrapper, that.defaultItemWrapper) && Objects.equals(defaultClickHandler, that.defaultClickHandler) && Objects.equals(slotSpecificItems, that.slotSpecificItems) && Objects.equals(slotClickHandlers, that.slotClickHandlers) && Objects.equals(slots, that.slots) && Objects.equals(oldSlots, that.oldSlots) && Objects.equals(itemHandler, that.itemHandler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gui, defaultItemWrapper, defaultClickHandler, slotSpecificItems, slotClickHandlers, slots, oldSlots, itemHandler);
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
         *
         * @param gui The parent GUI to associate with controller
         */
        public Builder(@NotNull AdvancedGui gui) {
            this.gui = gui;
        }

        /**
         * Sets default item for all slots.
         *
         * @param itemWrapper Default item to set
         * @return this builder for chaining
         */
        public Builder defaultItem(@Nullable ItemWrapper itemWrapper) {
            this.defaultItemWrapper = itemWrapper;
            return this;
        }

        /**
         * Sets default click handler for all slots.
         *
         * @param clickHandler Click handler to set
         * @return this builder for chaining
         */
        public Builder defaultClickHandler(AdvancedGuiClickHandler clickHandler) {
            this.defaultClickHandler = clickHandler;
            return this;
        }

        /**
         * Adds slots to be managed by controller.
         *
         * @param slots Array of slot indexes
         * @return this builder for chaining
         */
        public Builder slots(int... slots) {
            Arrays.stream(slots).forEach(this.slots::add);
            return this;
        }

        /**
         * Adds slots to be managed by controller.
         *
         * @param slots Collection of slot indexes
         * @return this builder for chaining
         */
        public Builder slots(@NotNull Collection<Integer> slots) {
            this.slots.addAll(slots);
            return this;
        }

        /**
         * Sets specific item for slot.
         *
         * @param slot        Slot index
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
         *
         * @param slot         Slot index
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
         *
         * @return Configured controller instance
         */
        public GuiItemController build() {
            GuiItemController controller = new GuiItemController(gui, defaultItemWrapper, defaultClickHandler, slots);
            slotSpecificItems.forEach(controller::setItem);
            slotClickHandlers.forEach(controller::setClickHandler);

            controller.itemHandler(itemHandlerKey);

            return controller;
        }
    }
}
