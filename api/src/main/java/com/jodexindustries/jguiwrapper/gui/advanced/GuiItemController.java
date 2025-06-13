package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GuiItemController {

    private final AdvancedGui gui;
    private final ItemWrapper itemWrapper;

    private int oldSlot = -1;
    private int slot;
    private InventoryHandler<InventoryClickEvent> clickHandler;

    public GuiItemController(@NotNull AdvancedGui gui, @NotNull ItemWrapper itemWrapper, int slot, InventoryHandler<InventoryClickEvent> clickHandler) {
        this.gui = gui;
        this.itemWrapper = itemWrapper;
        this.slot = slot;
        if (slot < 0 || slot >= 54)
            throw new IndexOutOfBoundsException("Slot " + slot + " is out of inventory bounds");
        this.clickHandler = clickHandler;
    }

    public void setSlot(int newSlot) {
        this.oldSlot = this.slot;
        this.slot = newSlot;
        redraw();
    }

    public void onClick(InventoryHandler<InventoryClickEvent> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public int getSlot() {
        return slot;
    }

    public @NotNull ItemWrapper getItemWrapper() {
        return itemWrapper;
    }

    public void redraw() {
        if (oldSlot != -1) {
            gui.removeClickHandlers(oldSlot);
            oldSlot = -1;
        }
        if (clickHandler != null) gui.setClickHandlers(clickHandler, slot);
        gui.holder().getInventory().setItem(slot, itemWrapper.itemStack());
    }

    public void updateItem(@NotNull Consumer<ItemWrapper> updater) {
        updater.accept(itemWrapper);
        redraw();
    }

    public static void swap(@NotNull GuiItemController a, @NotNull GuiItemController b) {
        int temp = a.slot;
        a.setSlot(b.slot);
        b.setSlot(temp);
    }
}
