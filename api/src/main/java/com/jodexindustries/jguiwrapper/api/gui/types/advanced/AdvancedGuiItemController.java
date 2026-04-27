package com.jodexindustries.jguiwrapper.api.gui.types.advanced;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.gui.types.SimpleGui;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.item.HandlerContext;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.item.ItemHandler;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.api.utils.Pair;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Consumer;

public class AdvancedGuiItemController<G extends SimpleGui<G> & AdvancedGui<G>, I extends ItemWrapper> {

    private final G gui;

    private I defaultItemWrapper;
    private AdvancedGuiClickHandler<G> defaultClickHandler;

    private final Map<Integer, I> slotSpecificItems = new HashMap<>();
    private final Map<Integer, AdvancedGuiClickHandler<G>> slotClickHandlers = new HashMap<>();

    private final Collection<Integer> defaultSlots;

    private final Set<Integer> slots = new LinkedHashSet<>();
    private final Set<Integer> oldSlots = new LinkedHashSet<>();

    private Pair<ItemHandler<?>, Class<?>> itemHandler;
    private Key itemHandlerKey;

    protected AdvancedGuiItemController(@NotNull G gui, @Nullable I defaultItemWrapper,
                                        @Nullable AdvancedGuiClickHandler<G> defaultClickHandler,
                                        @NotNull Collection<Integer> slots) {
        this.gui = gui;
        this.defaultItemWrapper = defaultItemWrapper;
        this.defaultClickHandler = defaultClickHandler;
        this.defaultSlots = slots;
    }

    public AdvancedGuiItemController(@NotNull G gui) {
        this(gui, null, null, Collections.emptySet());
    }

    public void drawSlots() {
        setSlots(this.defaultSlots);
    }

    public void addSlot(int slot) {
        validateSlot(slot);
        slots.add(slot);
        redraw();
    }

    public void removeSlot(int slot) {
        if (slots.remove(slot)) {
            slotSpecificItems.remove(slot);
            slotClickHandlers.remove(slot);
            clear(slot);
        }
    }

    public void setSlots(int... slots) {
        setSlots(Arrays.stream(slots).boxed().toList());
    }

    public void setSlots(@NotNull Collection<Integer> newSlots) {
        for (int slot : newSlots) {
            validateSlot(slot);
        }

        for (int slot : newSlots) {
            Optional<? extends AdvancedGuiItemController<?, ?>> existingController = gui.getController(slot);
            if (existingController.isPresent() && !existingController.get().equals(this)) {
                throw new IllegalArgumentException("Slot " + slot + " is already occupied by another controller");
            }
        }

        oldSlots.clear();
        oldSlots.addAll(this.slots);

        this.slots.clear();
        this.slots.addAll(newSlots);

        for (int slot : oldSlots) {
            this.gui.detach(slot);
        }
        for (int slot : this.slots) {
            this.gui.attach(this, slot);
        }
        redraw();
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= gui.size()) {
            throw new IndexOutOfBoundsException("Slot " + slot + " is out of inventory bounds");
        }
    }

    public boolean hasItem(int slot) {
        return slots.contains(slot) && getItem(slot) != null;
    }

    public boolean isEmpty() {
        return defaultItemWrapper == null && slotSpecificItems.isEmpty();
    }

    public void clear() {
        for (int slot : slots) {
            clear(slot);
        }
    }

    public void clear(int slot) {
        gui.removeClickHandlers(slot);
        gui.holder().clear(slot);
    }

    public void setItem(int slot, @NotNull I itemWrapper) {
        if (!slots.contains(slot)) {
            throw new IllegalArgumentException("Slot " + slot + " is not managed by this controller");
        }
        slotSpecificItems.put(slot, itemWrapper);
        redraw(slot);
    }

    public void removeItem(int slot) {
        slotSpecificItems.remove(slot);
        redraw(slot);
    }

    public void defaultItem(@Nullable I itemWrapper) {
        this.defaultItemWrapper = itemWrapper;
        redraw();
    }

    public @Nullable I getItem(int slot) {
        return slotSpecificItems.getOrDefault(slot, defaultItemWrapper);
    }

    public @Nullable I defaultItem() {
        return defaultItemWrapper;
    }

    public void setClickHandler(int slot, @Nullable AdvancedGuiClickHandler<G> clickHandler) {
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

    public @Nullable AdvancedGuiClickHandler<G> getClickHandler(int slot) {
        return slotClickHandlers.getOrDefault(slot, defaultClickHandler);
    }

    public void defaultClickHandler(@Nullable AdvancedGuiClickHandler<G> defaultClickHandler) {
        this.defaultClickHandler = defaultClickHandler;
    }

    public @Nullable AdvancedGuiClickHandler<G> defaultClickHandler() {
        return defaultClickHandler;
    }

    @UnmodifiableView
    public @NotNull Set<Integer> slots() {
        return Collections.unmodifiableSet(slots);
    }

    public @NotNull G gui() {
        return gui;
    }

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
        AdvancedGuiClickHandler<G> handler = getClickHandler(slot);
        if (handler != null) {
            gui.setClickHandlers(handler, slot);
        } else {
            gui.removeClickHandlers(slot);
        }

        I item = getItem(slot);
        if (item != null) {
            gui.holder().setItem(slot, item);
        }
    }

    public void updateItems(@NotNull Consumer<I> updater) {
        updateItems(updater, null);
    }

    public void updateItems(@NotNull Consumer<I> updater, @Nullable User user) {
        if (defaultItemWrapper != null) {
            updater.accept(defaultItemWrapper);
            if (!defaultItemWrapper.isUpdated()) defaultItemWrapper.update(user);
        }

        for (I item : slotSpecificItems.values()) {
            updater.accept(item);
            if (!item.isUpdated()) item.update(user);
        }

        redraw();
    }

    public void updateItem(int slot, @NotNull Consumer<I> updater) {
        updateItem(slot, updater, null);
    }

    public void updateItem(int slot, @NotNull Consumer<I> updater, @Nullable User user) {
        I item = getItem(slot);
        if (item != null) {
            updater.accept(item);
            if (!item.isUpdated()) item.update(user);
            redraw(slot);
        }
    }

    public void itemHandler(@Nullable Key itemHandlerKey) {
        this.itemHandlerKey = itemHandlerKey;
    }

    public @Nullable Key itemHandlerKey() {
        return itemHandlerKey;
    }

    public void attachResolvedHandler(@Nullable Pair<ItemHandler<?>, Class<?>> resolved) {
        this.itemHandler = resolved;
    }

    public void tryInvokeResolvedHandler(@NotNull LoadType loadType, @Nullable User user) {
        if (itemHandler == null) return;

        Class<?> expectedLoaderClass = itemHandler.b();

        gui.getLoader(expectedLoaderClass).ifPresent(loader -> {
            if (expectedLoaderClass.isInstance(loader)) {
                itemHandler.a().load(loader, this, new HandlerContext(loadType, user));
            }
        });
    }

    public static class Builder<G extends SimpleGui<G> & AdvancedGui<G>> {
        private final G gui;
        private ItemWrapper defaultItemWrapper;
        private AdvancedGuiClickHandler<G> defaultClickHandler;
        private final Set<Integer> slots = new LinkedHashSet<>();
        private final Map<Integer, ItemWrapper> slotSpecificItems = new HashMap<>();
        private final Map<Integer, AdvancedGuiClickHandler<G>> slotClickHandlers = new HashMap<>();
        private Key itemHandlerKey;

        public Builder(@NotNull G gui) {
            this.gui = gui;
        }

        public <I extends ItemWrapper> Builder<G> defaultItem(@Nullable I itemWrapper) {
            this.defaultItemWrapper = itemWrapper;
            return this;
        }

        public Builder<G> defaultClickHandler(@Nullable AdvancedGuiClickHandler<G> clickHandler) {
            this.defaultClickHandler = clickHandler;
            return this;
        }

        public Builder<G> slots(int... slots) {
            for (int slot : slots) this.slots.add(slot);
            return this;
        }

        public Builder<G> slots(@NotNull Collection<Integer> slots) {
            this.slots.addAll(slots);
            return this;
        }

        public <I extends ItemWrapper> Builder<G> slotItem(int slot, @NotNull I itemWrapper) {
            this.slotSpecificItems.put(slot, itemWrapper);
            this.slots.add(slot);
            return this;
        }

        public Builder<G> slotClickHandler(int slot, @Nullable AdvancedGuiClickHandler<G> clickHandler) {
            this.slotClickHandlers.put(slot, clickHandler);
            this.slots.add(slot);
            return this;
        }

        public Builder<G> itemHandler(@Nullable Key itemHandlerKey) {
            this.itemHandlerKey = itemHandlerKey;
            return this;
        }

        @SuppressWarnings("unchecked")
        public <I extends ItemWrapper> AdvancedGuiItemController<G, I> build() {
            AdvancedGuiItemController<G, I> controller = new AdvancedGuiItemController<>(
                    gui,
                    (I) defaultItemWrapper,
                    defaultClickHandler,
                    slots
            );

            slotSpecificItems.forEach((slot, item) -> controller.setItem(slot, (I) item));
            slotClickHandlers.forEach(controller::setClickHandler);
            controller.itemHandler(itemHandlerKey);
            return controller;
        }
    }
}

