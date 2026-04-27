package com.jodexindustries.jguiwrapper.api.gui.types;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiCloseEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiDragEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiOpenEvent;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Platform-independent base class for GUI implementations.
 */
public abstract class AbstractGui<T extends Gui> implements Gui {

    @NotNull
    protected SerializerType defaultSerializer = GuiApi.get().defaultSerializer();

    private int size;
    private Component title;

    public AbstractGui(@NotNull String title) {
        this(54, title);
    }

    public AbstractGui(int size, @NotNull String title) {
        this(size, GuiApi.get().defaultSerializer().deserialize(title));
    }

    public AbstractGui(@NotNull Component title) {
        this(54, title, null);
    }

    public AbstractGui(int size, @NotNull Component title) {
        this(size, title, null);
    }

    public AbstractGui(int size, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        this.size = adaptSize(size);
        this.title = title;
        if (defaultSerializer != null) {
            this.defaultSerializer = defaultSerializer;
        }

        INSTANCES.add(new WeakReference<>(this));
    }

    @Override
    public final int size() {
        return size;
    }

    public final void size(int size) {
        this.size = adaptSize(size);
    }

    @Override
    public final @NotNull Component title() {
        return title;
    }

    public final void title(@NotNull Component title) {
        this.title = title;
    }

    public final void title(@NotNull String title) {
        this.title = defaultSerializer.deserialize(title);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    protected T self() {
        return (T) this;
    }

    @ApiStatus.Internal
    public void onOpen(@NotNull GuiOpenEvent<T> event) {
    }

    @ApiStatus.Internal
    public void onClose(@NotNull GuiCloseEvent<T> event) {
    }

    @ApiStatus.Internal
    public void onClick(@NotNull GuiClickEvent<T> event) {
    }

    @ApiStatus.Internal
    public void onDrag(@NotNull GuiDragEvent<T> event) {
    }

    protected static int adaptSize(int size) {
        return ((Math.min(Math.max(size, 1), 54) + 8) / 9) * 9;
    }
}
