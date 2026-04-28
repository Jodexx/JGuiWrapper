package com.jodexindustries.jguiwrapper.minestom.gui.types;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiCloseEvent;
import com.jodexindustries.jguiwrapper.api.gui.types.SimpleGui;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.minestom.gui.MinestomGui;
import com.jodexindustries.jguiwrapper.minestom.gui.MinestomGuiHolder;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.play.OpenWindowPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class MinestomGuiBase<T extends Gui> extends SimpleGui<T> implements MinestomGui {

    private InventoryType type;

    private MinestomGuiHolder holder;

    public MinestomGuiBase(@NotNull String title) {
        super(title);
        init(null);
    }

    public MinestomGuiBase(int size, @NotNull String title) {
        super(size, title);
        init(null);
    }

    public MinestomGuiBase(@NotNull Component title) {
        this(InventoryType.CHEST_3_ROW, title);
    }

    public MinestomGuiBase(@NotNull InventoryType type, @NotNull Component title) {
        this(type, title, null);
    }

    public MinestomGuiBase(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(type.getSize(), title, defaultSerializer);
        init(type);
    }

    public MinestomGuiBase(int size, @NotNull Component title) {
        super(size, title);
        init(null);
    }

    private void init(@Nullable InventoryType inventoryType) {
        this.holder = new MinestomGuiHolder(this, inventoryType);
        this.type = holder.getInventory().getInventoryType();
    }

    public final void type(@NotNull InventoryType type) {
        this.type = type;
    }

    @NotNull
    public final InventoryType type() {
        return type;
    }

    @Override
    public @NotNull MinestomGuiHolder holder() {
        return this.holder;
    }

    @Override
    public void open(@NotNull Player player, @NotNull User user) {
        open(player, user, title());
    }

    @Override
    public void open(@NotNull Player player, @NotNull User user, @NotNull Component title) {
        Inventory inventory = holder.getInventory();
        player.openInventory(inventory);

        player.sendPacket(new OpenWindowPacket(inventory.getWindowId(), inventory.getInventoryType().getWindowType(), title));
        inventory.update(player);
    }

    @Override
    public void close(@NotNull Player player, @NotNull User user) {
        InventoryCloseEvent event = new InventoryCloseEvent(this.holder.getInventory(), player, false);

        onClose(new GuiCloseEvent(event, this, user));
        player.closeInventory();
    }

    @Override
    public void updateHolder() {
        Set<Player> viewers = this.holder.getInventory().getViewers();

        close();

        this.holder = new MinestomGuiHolder(this);

        viewers.forEach(this::open);
    }
}
