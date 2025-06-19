package com.jodexindustries.jguiwrapper.api.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface Gui {

    Set<WeakReference<Gui>> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @NotNull GuiHolder holder();

    @ApiStatus.Experimental
    void updateHolder();

    /**
     * @deprecated changing the title is not supported. This method has poorly defined and broken behaviors. It should not be used.
     * @since 1.20
     * @param title The new title.
     */
    @Deprecated(since = "1.21.1")
    default void setTitle(@NotNull HumanEntity player, @NotNull String title) {
        player.getOpenInventory().setTitle(title);
    }

    /**
     * @deprecated changing the title is not supported. This method has poorly defined and broken behaviors. It should not be used.
     * @since 1.20
     * @param title The new title.
     */
    @Deprecated(since = "1.21.1")
    default void setTitle(@NotNull String title) {
        holder().getInventory().getViewers().forEach(humanEntity -> setTitle(humanEntity, title));
    }

    void open(@NotNull HumanEntity player);

    void open(@NotNull HumanEntity player, Component title);

    default void close(@NotNull HumanEntity player) {
        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
    }

    default void close() {
        this.holder().getInventory().close();
    }

    static Set<Gui> getActiveInstances() {
        Set<Gui> active = new HashSet<>();
        for (WeakReference<Gui> ref : INSTANCES) {
            Gui instance = ref.get();
            if (instance != null) {
                active.add(instance);
            }
        }
        return active;
    }
}
