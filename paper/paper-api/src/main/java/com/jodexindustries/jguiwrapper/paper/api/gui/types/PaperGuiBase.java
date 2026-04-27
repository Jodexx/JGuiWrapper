package com.jodexindustries.jguiwrapper.paper.api.gui.types;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.types.SimpleGui;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiCloseEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiOpenEvent;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import com.jodexindustries.jguiwrapper.paper.api.gui.PaperGui;
import com.jodexindustries.jguiwrapper.paper.api.gui.PaperGuiHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class PaperGuiBase<T extends Gui> extends SimpleGui<T> implements PaperGui {

    public static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();

    protected static final PaperGuiApi API = PaperGuiApi.get();

    private InventoryType type;

    private PaperGuiHolder holder;

    public PaperGuiBase(@NotNull String title) {
        super(title);
        init(null);
    }

    public PaperGuiBase(int size, @NotNull String title) {
        super(size, title);
        init(null);
    }

    public PaperGuiBase(@NotNull Component title) {
        this(InventoryType.CHEST, title);
    }

    public PaperGuiBase(@NotNull InventoryType type, @NotNull Component title) {
        this(type, title, null);
    }

    public PaperGuiBase(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(type.getDefaultSize(), title, defaultSerializer);
        init(type);
    }

    public PaperGuiBase(int size, @NotNull Component title) {
        super(size, title);
        init(null);
    }

    private void init(@Nullable InventoryType inventoryType) {
        this.holder = new PaperGuiHolder(this, inventoryType);
        this.type = holder.getInventory().getType();
    }

    public final void type(@NotNull InventoryType type) {
        this.type = type;
    }

    @NotNull
    public final InventoryType type() {
        return type;
    }

    @Override
    public final @NotNull PaperGuiHolder holder() {
        return holder;
    }

    @Override
    public void open(@NotNull HumanEntity player, @NotNull User user) {
        open(player, user, title());
    }

    @Override
    public void open(@NotNull HumanEntity player, @NotNull User user, @NotNull Component title) {
        try {
            InventoryView view = API.getNMSWrapper().openInventory(player, holder.getInventory(), type, size(), title);

            if (view == null) {
                player.openInventory(holder.getInventory());
            } else {
                InventoryOpenEvent event = new InventoryOpenEvent(view);
                onOpen(new GuiOpenEvent(event, this, user));
            }
        } catch (Exception e) {
            API.getPlugin().getLogger().log(Level.WARNING, "Error with opening menu for player: " + player.getName(), e);
        }
    }

    @Override
    public void close(@NotNull HumanEntity player, @NotNull User user) {
        InventoryCloseEvent.Reason reason = InventoryCloseEvent.Reason.PLUGIN;
        player.closeInventory(reason);

        InventoryCloseEvent event = new InventoryCloseEvent(player.getOpenInventory());
        onClose(new GuiCloseEvent(event, this, user));
    }

    public final void updateMenu() {
        updateMenu(this.type, size(), title());
    }

    public final void updateMenu(boolean refreshData) {
        updateMenu(this.type, size(), title(), refreshData);
    }

    public final boolean updateMenu(@NotNull HumanEntity player) {
        return updateMenu(player, this.type, size(), title());
    }

    public final void updateMenu(@Nullable Component title) {
        updateMenu(null, size(), title);
    }

    public final void updateMenu(@Nullable InventoryType type) {
        updateMenu(type, size());
    }

    public final void updateMenu(@Nullable InventoryType type, int size) {
        updateMenu(type, size, null);
    }

    public final void updateMenu(@Nullable InventoryType type, int size, @Nullable Component title) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> updateMenu(humanEntity, type, size, title));
    }

    public final void updateMenu(@Nullable InventoryType type, int size, @Nullable Component title, boolean refreshData) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> updateMenu(humanEntity, type, size, title, refreshData));
    }

    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable Component title) {
        return updateMenu(player, null, size(), title);
    }

    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type) {
        return updateMenu(player, type, size(), null);
    }

    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size) {
        return updateMenu(player, type, size, null);
    }

    public final boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        return updateMenu(player, type, size, title, false);
    }

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

        this.holder = new PaperGuiHolder(this, type);

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
}
