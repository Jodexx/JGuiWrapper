package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.JGuiInitializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGui {

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

    public final @NotNull GuiHolder holder() {
        return holder;
    }

    /**
     * @deprecated changing the title is not supported. This method has poorly defined and broken behaviors. It should not be used.
     * @since 1.20
     * @param title The new title.
     */
    @Deprecated(since = "1.21.1")
    public final void setTitle(@NotNull HumanEntity player, @NotNull String title) {
        player.getOpenInventory().setTitle(title);
    }

    /**
     * @deprecated changing the title is not supported. This method has poorly defined and broken behaviors. It should not be used.
     * @since 1.20
     * @param title The new title.
     */
    @Deprecated(since = "1.21.1")
    public final void setTitle(@NotNull String title) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> setTitle(humanEntity, title));
    }

    public final void updateTitle() {
        updateTitle(this.title);
    }

    public final void updateTitle(@NotNull Component title) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> updateTitle(humanEntity, title));
    }

    public final void updateTitle(@NotNull HumanEntity player) {
        updateTitle(player, this.title);
    }

    public final void updateTitle(@NotNull HumanEntity player, @NotNull Component title) {
        JGuiInitializer.getNmsWrapper().updateTitle(player, title);
    }

    @ApiStatus.Experimental
    public final void updateHolder() {
        List<HumanEntity> viewers = new ArrayList<>(this.holder.getInventory().getViewers());

        this.holder.getInventory().close();

        this.holder = new GuiHolder(this);

        for (HumanEntity entity : viewers) {
            entity.openInventory(this.holder.getInventory());
        }
    }

    public void onOpen(@NotNull InventoryOpenEvent event) {

    }

    public void onClose(@NotNull InventoryCloseEvent event) {

    }

    public void onClick(@NotNull InventoryClickEvent event) {

    }

    public void onDrag(@NotNull InventoryDragEvent event) {

    }

    public final void open(@NotNull HumanEntity player) {
        player.openInventory(this.holder.getInventory());
    }

    public final void close(@NotNull HumanEntity player) {
        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
    }

    public final void close() {
        this.holder.getInventory().close();
    }

    private static int adaptSize(int size) {
        return ((Math.min(Math.max(size, 1), 54) + 8) / 9) * 9;
    }
}
