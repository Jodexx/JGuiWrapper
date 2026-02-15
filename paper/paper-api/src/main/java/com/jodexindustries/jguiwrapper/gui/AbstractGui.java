package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Abstract base class for all GUI implementations.
 * <p>
 * Provides common logic for inventory size, title, type, and holder management.
 * Handles registration of GUI instances and provides utility for legacy component serialization.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class AbstractGui implements Gui {

    public static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();

    /**
     * Main API instance for GUI operations.
     */
    protected static final GuiApi API = GuiApi.get();

    @NotNull
    protected SerializerType defaultSerializer = API.defaultSerializer();

    private int size;
    private Component title;
    private InventoryType type;

    private GuiHolder holder;

    /**
     * Constructs a GUI with the default size (54) and a string title.
     *
     * @param title The GUI title as a string
     */
    public AbstractGui(@NotNull String title) {
        this(54, title);
    }

    /**
     * Constructs a GUI with a specific size and string title.
     *
     * @param size  The inventory size
     * @param title The GUI title as a string
     */
    public AbstractGui(int size, @NotNull String title) {
        this(size, API.defaultSerializer().deserialize(title));
    }

    /**
     * Constructs a GUI with the default CHEST type and a component title.
     *
     * @param title The GUI title as a Component
     */
    public AbstractGui(@NotNull Component title) {
        this(InventoryType.CHEST, title);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title.
     *
     * @param type  The inventory type
     * @param title The GUI title as a Component
     */
    public AbstractGui(@NotNull InventoryType type, @NotNull Component title) {
        this(type, title, null);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title with serializer.
     *
     * @param type              The inventory type.
     * @param title             The GUI title as a {@link Component}.
     * @param defaultSerializer The default serializer used for converting between plain strings and {@link Component}
     *                          instances. If {@code null}, the
     *                          {@link #defaultSerializer} will be used.
     */
    public AbstractGui(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        this(type.getDefaultSize(), type, title, defaultSerializer);
    }

    /**
     * Constructs a GUI with a specific size, optional type, and component title.
     *
     * @param size  The inventory size
     * @param title The GUI title as a Component
     */
    public AbstractGui(int size, @NotNull Component title) {
        this(size, null, title, null);
    }

    private AbstractGui(int size, @Nullable InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        this.size = adaptSize(size);
        this.title = title;
        this.holder = new GuiHolder(this, type);
        this.type = holder.getInventory().getType();
        if (defaultSerializer != null) {
            this.defaultSerializer = defaultSerializer;
        }

        INSTANCES.add(new WeakReference<>(this));
    }

    /**
     * Gets the inventory size (number of slots).
     *
     * @return the inventory size
     */
    public final int size() {
        return size;
    }

    /**
     * Sets the inventory size (number of slots).
     *
     * @param size the new inventory size
     */
    public final void size(int size) {
        this.size = adaptSize(size);
    }

    /**
     * Gets the GUI title as a Component.
     *
     * @return the GUI title
     */
    public final @NotNull Component title() {
        return title;
    }

    /**
     * Sets the GUI title as a Component.
     *
     * @param title the new GUI title
     */
    public final void title(@NotNull Component title) {
        this.title = title;
    }

    /**
     * Sets the GUI title from a legacy string.
     *
     * @param title the new GUI title as a string
     */
    public final void title(@NotNull String title) {
        this.title = defaultSerializer.deserialize(title);
    }

    /**
     * Sets the inventory type (e.g., CHEST, HOPPER).
     *
     * @param type the new inventory type
     */
    public final void type(@NotNull InventoryType type) {
        this.type = type;
    }

    /**
     * Gets the inventory type (e.g., CHEST, HOPPER).
     *
     * @return the inventory type
     */
    @NotNull
    public final InventoryType type() {
        return type;
    }

    @Override
    public final @NotNull GuiHolder holder() {
        return holder;
    }

    @Override
    public final void open(@NotNull HumanEntity player) {
        open(player, title);
    }

    @Override
    public final void open(@NotNull HumanEntity player, @NotNull Component title) {
        try {
            InventoryView view = API.getNMSWrapper().openInventory(player, holder.getInventory(), type, size, title);

            if (view == null) {
                player.openInventory(holder.getInventory());
            } else {
                onOpen(new InventoryOpenEvent(view));
            }
        } catch (Exception e) {
            API.getPlugin().getLogger().log(Level.WARNING, "Error with opening menu for player: " + player.getName(), e);
        }
    }

    @Override
    public final void close(@NotNull HumanEntity player) {
        InventoryCloseEvent.Reason reason = InventoryCloseEvent.Reason.PLUGIN;
        player.closeInventory(reason);
        onClose(new InventoryCloseEvent(player.getOpenInventory(), reason));
    }

    /**
     * Called when the GUI is opened for a player.
     *
     * @param event the InventoryOpenEvent
     */
    @ApiStatus.Internal
    public void onOpen(@NotNull InventoryOpenEvent event) {
    }

    /**
     * Called when the GUI is closed for a player.
     *
     * @param event the InventoryCloseEvent
     */
    @ApiStatus.Internal
    public void onClose(@NotNull InventoryCloseEvent event) {
    }

    /**
     * Called when a player clicks in the GUI.
     *
     * @param event the InventoryClickEvent
     */
    @ApiStatus.Internal
    public void onClick(@NotNull InventoryClickEvent event) {
    }

    /**
     * Called when a player drags items in the GUI.
     *
     * @param event the InventoryDragEvent
     */
    @ApiStatus.Internal
    public void onDrag(@NotNull InventoryDragEvent event) {
    }

    /**
     * Updates the menu for all viewers with the current type, size, and title.
     */
    public final void updateMenu() {
        updateMenu(this.type, this.size, this.title);
    }

    /**
     * Updates the menu for all viewers with the current type, size, and title.
     *
     * @param refreshData whether to refresh the menu's data
     */
    public final void updateMenu(boolean refreshData) {
        updateMenu(this.type, this.size, this.title, refreshData);
    }

    /**
     * Updates the menu for a specific player with the current type, size, and title.
     *
     * @param player the player to update the menu for
     * @return true if the menu was updated successfully, false otherwise
     */
    public final boolean updateMenu(@NotNull HumanEntity player) {
        return updateMenu(player, this.type, this.size, this.title);
    }

    /**
     * Updates the menu for all viewers with a new title, keeping the current type and size.
     *
     * @param title the new title for the menu
     */
    public final void updateMenu(@Nullable Component title) {
        updateMenu(null, this.size, title);
    }

    /**
     * Updates the menu for all viewers with a new type, keeping the current size and title.
     *
     * @param type the new inventory type
     */
    public final void updateMenu(@Nullable InventoryType type) {
        updateMenu(type, this.size);
    }

    /**
     * Updates the menu for all viewers with a new type and size, keeping the current title.
     *
     * @param type the new inventory type
     * @param size the new inventory size
     */
    public final void updateMenu(@Nullable InventoryType type, int size) {
        updateMenu(type, size, null);
    }

    /**
     * Updates the menu for all viewers with new type, size, and title.
     *
     * @param type  the new inventory type
     * @param size  the new inventory size
     * @param title the new title for the menu
     */
    public final void updateMenu(@Nullable InventoryType type, int size, @Nullable Component title) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> updateMenu(humanEntity, type, size, title));
    }

    /**
     * Updates the menu for all viewers with new type, size, and title.
     *
     * @param type        the new inventory type
     * @param size        the new inventory size
     * @param title       the new title for the menu
     * @param refreshData whether to refresh the menu's data
     */
    public final void updateMenu(@Nullable InventoryType type, int size, @Nullable Component title, boolean refreshData) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> updateMenu(humanEntity, type, size, title, refreshData));
    }

    /**
     * Updates the menu for a specific player with a new title, keeping the current type and size.
     *
     * @param player the player to update the menu for
     * @param title  the new title for the menu
     * @return true if the menu was updated successfully, false otherwise
     */
    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable Component title) {
        return updateMenu(player, null, this.size, title);
    }

    /**
     * Updates the menu for a specific player with a new type, keeping the current size and title.
     *
     * @param player the player to update the menu for
     * @param type   the new inventory type
     * @return true if the menu was updated successfully, false otherwise
     */
    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type) {
        return updateMenu(player, type, this.size, null);
    }

    /**
     * Updates the menu for a specific player with a new type and size, keeping the current title.
     *
     * @param player the player to update the menu for
     * @param type   the new inventory type
     * @param size   the new inventory size
     * @return true if the menu was updated successfully, false otherwise
     */
    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size) {
        return updateMenu(player, type, size, null);
    }

    /**
     * Updates the menu for a specific player with new type, size, and title.
     *
     * @param player the player to update the menu for
     * @param type   the new inventory type
     * @param size   the new inventory size
     * @param title  the new title for the menu
     * @return true if the menu was updated successfully, false otherwise
     */
    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        return updateMenu(player, type, size, title, false);
    }

    /**
     * Updates the menu for a specific player with new type, size, and title.
     *
     * @param player      the player to update the menu for
     * @param type        the new inventory type
     * @param size        the new inventory size
     * @param title       the new title for the menu
     * @param refreshData whether to refresh the menu's data
     * @return true if the menu was updated successfully, false otherwise
     */
    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title, boolean refreshData) {
        try {
            return API.getNMSWrapper().updateMenu(player, type, size, title, refreshData);
        } catch (Exception e) {
            API.getPlugin().getLogger().log(Level.WARNING, "Error with updating menu for player: " + player.getName(), e);
            return false;
        }
    }

    @Override
    public final void updateHolder() {
        List<HumanEntity> viewers = new ArrayList<>(this.holder.getInventory().getViewers());

        close();

        this.holder = new GuiHolder(this, type);

        viewers.forEach(this::open);
    }

    public final BukkitTask runTask(@NotNull Runnable task) {
        return SCHEDULER.runTask(API.getPlugin(), task);
    }

    public final BukkitTask runTaskAsync(@NotNull Runnable task) {
        return SCHEDULER.runTaskAsynchronously(API.getPlugin(), task);
    }

    public final void runTask(@NotNull Consumer<BukkitTask> task) {
        SCHEDULER.runTask(API.getPlugin(), task);
    }

    public final void runTaskAsync(@NotNull Consumer<BukkitTask> task) {
        SCHEDULER.runTaskAsynchronously(API.getPlugin(), task);
    }

    public final BukkitTask runTask(@NotNull Runnable task, long delay) {
        return SCHEDULER.runTaskLater(API.getPlugin(), task, delay);
    }

    public final BukkitTask runTaskAsync(@NotNull Runnable task, long delay) {
        return SCHEDULER.runTaskLaterAsynchronously(API.getPlugin(), task, delay);
    }

    public final void runTask(@NotNull Consumer<BukkitTask> task, long delay) {
        SCHEDULER.runTaskLater(API.getPlugin(), task, delay);
    }

    public final void runTaskAsync(@NotNull Consumer<BukkitTask> task, long delay) {
        SCHEDULER.runTaskLaterAsynchronously(API.getPlugin(), task, delay);
    }

    public final BukkitTask runTask(@NotNull Runnable task, long delay, long period) {
        return SCHEDULER.runTaskTimer(API.getPlugin(), task, delay, period);
    }

    public final BukkitTask runTaskAsync(@NotNull Runnable task, long delay, long period) {
        return SCHEDULER.runTaskLaterAsynchronously(API.getPlugin(), task, delay);
    }

    public final void runTask(@NotNull Consumer<BukkitTask> task, long delay, long period) {
        SCHEDULER.runTaskTimer(API.getPlugin(), task, delay, period);
    }

    public final void runTaskAsync(@NotNull Consumer<BukkitTask> task, long delay, long period) {
        SCHEDULER.runTaskTimerAsynchronously(API.getPlugin(), task, delay, period);
    }

    /**
     * Adapts the given size to the nearest valid inventory size (multiple of 9, between 1 and 54).
     *
     * @param size the requested inventory size
     * @return the adapted inventory size
     */
    protected static int adaptSize(int size) {
        return ((Math.min(Math.max(size, 1), 54) + 8) / 9) * 9;
    }

}
