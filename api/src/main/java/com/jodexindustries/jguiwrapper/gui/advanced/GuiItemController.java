package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GuiItemController {

    private final AdvancedGui gui;
    private final ItemWrapper itemWrapper;

    private final Set<Integer> slots = new LinkedHashSet<>();
    private final Set<Integer> oldSlots = new LinkedHashSet<>();

    private InventoryHandler<InventoryClickEvent> clickHandler;

    public GuiItemController(@NotNull AdvancedGui gui, @NotNull ItemWrapper itemWrapper, InventoryHandler<InventoryClickEvent> clickHandler, @NotNull Collection<Integer> slots) {
        this.gui = gui;
        this.itemWrapper = itemWrapper;
        this.clickHandler = clickHandler;
        setSlots(slots);
    }

    public GuiItemController(@NotNull AdvancedGui gui, @NotNull ItemWrapper itemWrapper, InventoryHandler<InventoryClickEvent> clickHandler, int @NotNull ... slots) {
        this.gui = gui;
        this.itemWrapper = itemWrapper;
        this.clickHandler = clickHandler;
        setSlots(slots);
    }

    public void addSlot(Integer slot) {
        if (slot < 0 || slot >= 54)
            throw new IndexOutOfBoundsException("Slot " + slot + " is out of inventory bounds");
        slots.add(slot);
        redraw();
    }

    public void removeSlot(Integer slot) {
        if (slots.remove(slot)) {
            gui.removeClickHandlers(slot);
            gui.holder().getInventory().clear(slot);
        }
    }

    public void setSlots(int... slots) {
        List<Integer> list = Arrays.stream(slots).boxed().collect(Collectors.toList());

        setSlots(list);
    }

    public void setSlots(@NotNull Collection<Integer> newSlots) {
        for (int slot : slots) {
            if (slot < 0 || slot >= 54)
                throw new IndexOutOfBoundsException("Slot " + slot + " is out of inventory bounds");

            for (GuiItemController value : gui.getControllers()) {
                if (!value.slots.contains(slot)) {
                    this.slots.add(slot);
                }
            }
        }

        oldSlots.clear();
        oldSlots.addAll(slots);
        slots.clear();
        slots.addAll(newSlots);
        redraw();
    }

    public void onClick(InventoryHandler<InventoryClickEvent> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public @NotNull Set<Integer> getSlots() {
        return Collections.unmodifiableSet(slots);
    }

    public @NotNull ItemWrapper getItemWrapper() {
        return itemWrapper;
    }

    public InventoryHandler<InventoryClickEvent> getClickHandler() {
        return clickHandler;
    }

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
        if (clickHandler != null) gui.setClickHandlers(clickHandler, slot);
        gui.holder().getInventory().setItem(slot, itemWrapper.itemStack());
    }

    public void updateItem(@NotNull Consumer<ItemWrapper> updater) {
        updater.accept(itemWrapper);

        if (!itemWrapper.isUpdated()) itemWrapper.update();

        redraw();
    }

    public static void swap(@NotNull GuiItemController a, @NotNull GuiItemController b) {
        Set<Integer> temp = new LinkedHashSet<>(a.slots);
        a.setSlots(b.slots);
        b.setSlots(temp);
    }
}
