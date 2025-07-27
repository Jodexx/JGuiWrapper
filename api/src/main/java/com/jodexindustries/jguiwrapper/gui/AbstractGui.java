package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Abstract base class for all GUI implementations.
 * <p>
 * Provides common logic for inventory size, title, type, and holder management.
 * Handles registration of GUI instances and provides utility for legacy component serialization.
 */
@SuppressWarnings({"unused"})
public abstract class AbstractGui implements Gui {
    /**
     * LegacyComponentSerializer using ampersand (&amp;) as the color code character.
     */
    public static final LegacyComponentSerializer LEGACY_AMPERSAND = LegacyComponentSerializer.legacyAmpersand();

    @NotNull
    protected SerializerType defaultSerizalizer = SerializerType.LEGACY;

    /**
     * Main API instance for GUI operations.
     */
    protected static final GuiApi API = GuiApi.get();
    /**
     * NMS wrapper for version-dependent operations.
     */
    protected static final NMSWrapper NMS_WRAPPER = API.getNMSWrapper();

    private int size;
    private Component title;
    private InventoryType type;

    private GuiHolder holder;

    /**
     * Constructs a GUI with the default size (54) and a string title.
     * @param title The GUI title as a string (legacy color codes supported)
     */
    public AbstractGui(@NotNull String title) {
        this(54, title);
    }

    /**
     * Constructs a GUI with a specific size and string title.
     * @param size  The inventory size
     * @param title The GUI title as a string (legacy color codes supported)
     */
    public AbstractGui(int size, @NotNull String title) {
        this(size, LEGACY_AMPERSAND.deserialize(title));
    }

    /**
     * Constructs a GUI with the default CHEST type and a component title.
     * @param title The GUI title as a Component
     */
    public AbstractGui(@NotNull Component title) {
        this(InventoryType.CHEST, title);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title.
     * @param type  The inventory type
     * @param title The GUI title as a Component
     */
    public AbstractGui(@NotNull InventoryType type, @NotNull Component title) {
        this(type.getDefaultSize(), type, title, null);
    }

    /**
     * Constructs a GUI with a specific size, optional type, and component title.
     * @param size  The inventory size
     * @param title The GUI title as a Component
     */
    public AbstractGui(int size, @NotNull Component title) {
        this(size, null, title, null);
    }

    private AbstractGui(int size, @Nullable InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerizalizer) {
        this.size = adaptSize(size);
        this.title = title;
        this.holder = new GuiHolder(this, type);
        this.type = holder.getInventory().getType();
        if (defaultSerizalizer != null) {
            this.defaultSerizalizer = defaultSerizalizer;
        }

        INSTANCES.add(new WeakReference<>(this));
    }

    /**
     * Gets the inventory size (number of slots).
     * @return the inventory size
     */
    public final int size() {
        return size;
    }

    /**
     * Sets the inventory size (number of slots).
     * @param size the new inventory size
     */
    public final void size(int size) {
        this.size = adaptSize(size);
    }

    /**
     * Gets the GUI title as a Component.
     * @return the GUI title
     */
    public final @NotNull Component title() {
        return title;
    }

    /**
     * Sets the GUI title as a Component.
     * @param title the new GUI title
     */
    public final void title(@NotNull Component title) {
        this.title = title;
    }

    /**
     * Sets the GUI title from a legacy string.
     * @param title the new GUI title as a string (legacy color codes supported)
     */
    public final void title(@NotNull String title) {
        this.title = defaultSerizalizer.deserialize(title);
    }

    /**
     * Sets the inventory type (e.g., CHEST, HOPPER).
     * @param type the new inventory type
     */
    public final void type(@NotNull InventoryType type) {
        this.type = type;
    }

    /**
     * Gets the inventory type (e.g., CHEST, HOPPER).
     * @return the inventory type
     */
    @NotNull
    public final InventoryType type() {
        return type;
    }

    /**
     * Gets the holder for this GUI instance.
     * @return the GuiHolder for this GUI
     */
    @Override
    public final @NotNull GuiHolder holder() {
        return holder;
    }

    /**
     * Opens this GUI for the specified player.
     * @param player the player to open the GUI for
     */
    @Override
    public final void open(@NotNull HumanEntity player) {
        open(player, title);
    }

    /**
     * Opens this GUI for the specified player with a custom title.
     * @param player the player to open the GUI for
     * @param title the custom title to use
     */
    @Override
    public final void open(@NotNull HumanEntity player, Component title) {
        try {
            InventoryView view = NMS_WRAPPER.openInventory(player, holder.getInventory(), type, size, title);

            if (view == null) {
                player.openInventory(holder.getInventory());
            } else {
                onOpen(new InventoryOpenEvent(view));
            }
        } catch (Exception e) {
            API.getPlugin().getLogger().log(Level.WARNING, "Error with opening menu for player: " + player.getName(), e);
        }
    }

    /**
     * Closes this GUI for the specified player.
     * @param player the player to close the GUI for
     */
    @Override
    public final void close(@NotNull HumanEntity player) {
        InventoryCloseEvent.Reason reason = InventoryCloseEvent.Reason.PLUGIN;
        onClose(new InventoryCloseEvent(player.getOpenInventory(), reason));
        player.closeInventory(reason);
    }

    /**
     * Called when the GUI is opened for a player. Can be overridden by subclasses.
     * @param event the InventoryOpenEvent
     */
    public void onOpen(@NotNull InventoryOpenEvent event) {}

    /**
     * Called when the GUI is closed for a player. Can be overridden by subclasses.
     * @param event the InventoryCloseEvent
     */
    public void onClose(@NotNull InventoryCloseEvent event) {}

    /**
     * Called when a player clicks in the GUI. Can be overridden by subclasses.
     * @param event the InventoryClickEvent
     */
    public void onClick(@NotNull InventoryClickEvent event) {}

    /**
     * Called when a player drags items in the GUI. Can be overridden by subclasses.
     * @param event the InventoryDragEvent
     */
    public void onDrag(@NotNull InventoryDragEvent event) {}

    /**
     * Updates the menu for all viewers with the current type, size, and title.
     */
    public final void updateMenu() {
        updateMenu(this.type, this.size, this.title);
    }

    /**
     * Updates the menu for a specific player with the current type, size, and title.
     * @param player the player to update the menu for
     */
    public final boolean updateMenu(HumanEntity player) {
        return updateMenu(player, this.type, this.size, this.title);
    }

    /**
     * Updates the menu for all viewers with a new title, keeping the current type and size.
     * @param title the new title for the menu
     */
    public final void updateMenu(Component title) {
        updateMenu(null, this.size, title);
    }

    /**
     * Updates the menu for all viewers with a new type, keeping the current size and title.
     * @param type the new inventory type
     */
    public final void updateMenu(InventoryType type) {
        updateMenu(type, this.size);
    }

    /**
     * Updates the menu for all viewers with a new type and size, keeping the current title.
     * @param type the new inventory type
     * @param size the new inventory size
     */
    public final void updateMenu(InventoryType type, int size) {
        updateMenu(type, size, null);
    }

    /**
     * Updates the menu for all viewers with new type, size, and title.
     * @param type the new inventory type
     * @param size the new inventory size
     * @param title the new title for the menu
     */
    public final void updateMenu(InventoryType type, int size, Component title) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> updateMenu(humanEntity, type, size, title));
    }

    /**
     * Updates the menu for a specific player with a new title, keeping the current type and size.
     * @param player the player to update the menu for
     * @param title the new title for the menu
     */
    public final boolean updateMenu(@NotNull HumanEntity player, Component title) {
        return updateMenu(player, null, this.size, title);
    }

    /**
     * Updates the menu for a specific player with a new type, keeping the current size and title.
     * @param player the player to update the menu for
     * @param type the new inventory type
     */
    public final boolean updateMenu(@NotNull HumanEntity player, InventoryType type) {
        return updateMenu(player, type, this.size, null);
    }

    /**
     * Updates the menu for a specific player with a new type and size, keeping the current title.
     * @param player the player to update the menu for
     * @param type the new inventory type
     * @param size the new inventory size
     */
    public final boolean updateMenu(@NotNull HumanEntity player, InventoryType type, int size) {
        return updateMenu(player, type, size, null);
    }

    /**
     * Updates the menu for a specific player with new type, size, and title.
     * @param player the player to update the menu for
     * @param type the new inventory type
     * @param size the new inventory size
     * @param title the new title for the menu
     */
    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        try {
            return NMS_WRAPPER.updateMenu(player, type, size, title);
        } catch (Exception e) {
            API.getPlugin().getLogger().log(Level.WARNING, "Error with updating menu for player: " + player.getName(), e);
            return false;
        }
    }

    /**
     * Updates the holder for this GUI instance and reopens the GUI for all viewers.
     */
    @Override
    public final void updateHolder() {
        List<HumanEntity> viewers = new ArrayList<>(this.holder.getInventory().getViewers());

        close();

        this.holder = new GuiHolder(this, type);

        viewers.forEach(this::open);
    }

    /**
     * Adapts the given size to the nearest valid inventory size (multiple of 9, between 1 and 54).
     * @param size the requested inventory size
     * @return the adapted inventory size
     */
    protected static int adaptSize(int size) {
        return ((Math.min(Math.max(size, 1), 54) + 8) / 9) * 9;
    }

}
