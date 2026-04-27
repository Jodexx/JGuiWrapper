package com.jodexindustries.jguiwrapper.api.gui.types;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.GuiHandler;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiCloseEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiDragEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiOpenEvent;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Platform-independent simple GUI with slot handlers and event consumers.
 */
public abstract class SimpleGui<T extends Gui> extends AbstractGui<T> {

    private final Map<Integer, GuiHandler<GuiClickEvent<T>, T>> slotClickHandlers = new HashMap<>();

    private final List<Consumer<GuiOpenEvent<T>>> openEventConsumers = new ArrayList<>();
    private final List<Consumer<GuiCloseEvent<T>>> closeEventConsumers = new ArrayList<>();
    private final List<Consumer<GuiDragEvent<T>>> dragEventConsumers = new ArrayList<>();

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

    public SimpleGui(int size, @NotNull Component title) {
        super(size, title);
    }

    public SimpleGui(int size, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(size, title, defaultSerializer);
    }

    @Override
    public void onOpen(@NotNull GuiOpenEvent<T> event) {
        for (Consumer<GuiOpenEvent<T>> consumer : openEventConsumers) {
            consumer.accept(event);
        }
    }

    @Override
    public void onClose(@NotNull GuiCloseEvent<T> event) {
        for (Consumer<GuiCloseEvent<T>> consumer : closeEventConsumers) {
            consumer.accept(event);
        }
    }

    @Override
    public void onDrag(@NotNull GuiDragEvent<T> event) {
        if (cancelEmptySlots) {
            for (Integer rawSlot : event.rawSlots()) {
                if (rawSlot < size()) {
                    event.setCancelled(true);
                }
            }
        }

        for (Consumer<GuiDragEvent<T>> consumer : dragEventConsumers) {
            consumer.accept(event);
        }
    }

    @Override
    public void onClick(@NotNull GuiClickEvent<T> event) {
        final int slot = event.rawSlot();

        if (slot < 0) return;

        if (event.action() == GuiClickEvent.InventoryAction.MOVE_TO_OTHER_INVENTORY || event.action() == GuiClickEvent.InventoryAction.COLLECT_TO_CURSOR) {
            if (event.playerInventory() && cancelEmptySlots) {
                event.setCancelled(true);
            }
        }

        final GuiHandler<GuiClickEvent<T>, T> handler = slotClickHandlers.get(slot);

        if (handler != null) {
            handler.handle(event, self());
        } else if (slot < size() && cancelEmptySlots) {
            event.setCancelled(true);
        }
    }

    public final void onOpen(@NotNull Consumer<GuiOpenEvent<T>> consumer) {
        this.openEventConsumers.add(consumer);
    }

    public final void onClose(@NotNull Consumer<GuiCloseEvent<T>> consumer) {
        this.closeEventConsumers.add(consumer);
    }

    public final void onDrag(@NotNull Consumer<GuiDragEvent<T>> consumer) {
        this.dragEventConsumers.add(consumer);
    }

    public void setClickHandlers(@NotNull GuiHandler<GuiClickEvent<T>, T> handler, int @NotNull ... slots) {
        if (slots.length == 0) {
            setClickHandlers0(handler, IntStream.range(0, super.size()).toArray());
            return;
        }

        setClickHandlers0(handler, slots);
    }

    private void setClickHandlers0(@NotNull GuiHandler<GuiClickEvent<T>, T> handler, int @NotNull ... slots) {
        for (int slot : slots) {
            slotClickHandlers.put(slot, handler);
        }
    }

    public void removeClickHandlers(int @NotNull ... slots) {
        if (slots.length == 0) {
            slotClickHandlers.clear();
            return;
        }

        for (int slot : slots) {
            slotClickHandlers.remove(slot);
        }
    }

    public void setCancelEmptySlots(boolean cancelEmptySlots) {
        this.cancelEmptySlots = cancelEmptySlots;
    }
}
