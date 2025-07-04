package com.jodexindustries.jguiwrapper.api.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface Gui {

    Set<WeakReference<Gui>> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @NotNull GuiHolder holder();

    @ApiStatus.Experimental
    void updateHolder();

    void open(@NotNull HumanEntity player);

    void open(@NotNull HumanEntity player, Component title);

    void close(@NotNull HumanEntity player);

    default void close() {
        new ArrayList<>(holder().getInventory().getViewers()).forEach(this::close);
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
