package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public abstract class SimpleGui extends AbstractGui {

    private final Map<Integer, InventoryHandler<InventoryClickEvent>> slotClickHandlers = new HashMap<>();

    private Consumer<InventoryOpenEvent> openEventConsumer;
    private Consumer<InventoryCloseEvent> closeEventConsumer;
    private Consumer<InventoryDragEvent> dragEventConsumer;

    public SimpleGui(@NotNull String title) {
        super(title);
    }

    public SimpleGui(int size, @NotNull String title) {
        super(size, title);
    }

    public SimpleGui(@NotNull Component title) {
        super(title);
    }

    public SimpleGui(int size, @NotNull Component title) {
        super(size, title);
    }

    @Override
    final void onOpen(@NotNull InventoryOpenEvent event) {
        super.onOpen(event);

        if (openEventConsumer != null) openEventConsumer.accept(event);
    }

    @Override
    final void onClose(@NotNull InventoryCloseEvent event) {
        if (closeEventConsumer != null) closeEventConsumer.accept(event);
    }

    @Override
    final void onDrag(@NotNull InventoryDragEvent event) {
        if (dragEventConsumer != null) dragEventConsumer.accept(event);
    }

    @Override
    final void onClick(@NotNull InventoryClickEvent event) {
        int slot = event.getRawSlot();

        InventoryHandler<InventoryClickEvent> handler = slotClickHandlers.get(slot);

        if (handler != null) {
            handler.handle(event);
            event.setCancelled(true);
        }
    }

    public final void onOpen(Consumer<InventoryOpenEvent> consumer) {
        this.openEventConsumer = consumer;
    }

    public final void onClose(Consumer<InventoryCloseEvent> consumer) {
        this.closeEventConsumer = consumer;
    }

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

}