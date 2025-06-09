package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGui implements Gui {

    protected static final LegacyComponentSerializer LEGACY_AMPERSAND = LegacyComponentSerializer.legacyAmpersand();

    private int size;
    private Component title;
    private GuiHolder holder;

    public AbstractGui(@NotNull String title) {
        this(54, title);
    }

    public AbstractGui(int size, @NotNull String title) {
        this(size, LEGACY_AMPERSAND.deserialize(title));
    }

    public AbstractGui(@NotNull Component title) {
        this(54, title);
    }

    public AbstractGui(int size, @NotNull Component title) {
        this.size = adaptSize(size);
        this.title = title;
        this.holder = new GuiHolder(this);
    }

    public final int size() {
        return size;
    }

    public final void size(int size) {
        this.size = adaptSize(size);
    }

    public final @NotNull Component title() {
        return title;
    }

    public final void title(@NotNull Component title) {
        this.title = title;
    }

    public final void title(@NotNull String title) {
        this.title = LEGACY_AMPERSAND.deserialize(title);
    }

    @Override
    public final @NotNull GuiHolder holder() {
        return holder;
    }

    public final void updateMenu(Component title) {
        updateMenu(null, this.size, title);
    }

    public final void updateMenu(InventoryType type) {
        updateMenu(type, this.size);
    }

    public final void updateMenu(InventoryType type, int size) {
        updateMenu(type, size, null);
    }

    public final void updateMenu(InventoryType type, int size, Component title) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> updateMenu(humanEntity, type, size, title));
    }

    public final void updateMenu(@NotNull HumanEntity player, Component title) {
        updateMenu(player, null, this.size, title);
    }

    public final void updateMenu(@NotNull HumanEntity player, InventoryType type) {
        updateMenu(player, type, this.size, null);
    }

    public final void updateMenu(@NotNull HumanEntity player, InventoryType type, int size) {
        updateMenu(player, type, size, null);
    }

    public final void updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        GuiApi.get().getNMSWrapper().updateMenu(player, type, size, title);
    }

    @Override
    public final void updateHolder() {
        List<HumanEntity> viewers = new ArrayList<>(this.holder.getInventory().getViewers());

        this.holder.getInventory().close();

        this.holder = new GuiHolder(this);

        for (HumanEntity entity : viewers) {
            entity.openInventory(this.holder.getInventory());
        }
    }

    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {

    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {

    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {

    }

    @Override
    public void onDrag(@NotNull InventoryDragEvent event) {

    }

    protected static int adaptSize(int size) {
        return ((Math.min(Math.max(size, 1), 54) + 8) / 9) * 9;
    }
}
