package com.jodexindustries.jguiwrapper.common.gui;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiCloseEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiDragEvent;
import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import com.jodexindustries.jguiwrapper.paper.api.gui.PaperGuiHolder;
import com.jodexindustries.jguiwrapper.paper.gui.AbstractGui;
import com.jodexindustries.jguiwrapper.paper.api.utils.GuiUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

@SuppressWarnings({"unused", "unchecked"})
public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        PaperGuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        boolean playerInventory = e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER;

        AbstractGui<Gui> gui = (AbstractGui<Gui>) holder.gui();

        GuiClickEvent<Gui> event = new GuiClickEvent<>(
                e, gui,
                PaperGuiApi.get().user(e.getWhoClicked()),
                e.getRawSlot(),
                playerInventory,
                GuiClickEvent.InventoryAction.valueOf(e.getAction().name()), // action
                GuiClickEvent.ClickType.valueOf(e.getClick().name()) // click
        );
        gui.onClick(event);
        if (event.isCancelled()) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getReason() == InventoryCloseEvent.Reason.PLUGIN) return;

        PaperGuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        AbstractGui<Gui> gui = (AbstractGui<Gui>) holder.gui();

        GuiCloseEvent<Gui> event = new GuiCloseEvent<>(e, gui, PaperGuiApi.get().user(e.getPlayer()));
        gui.onClose(event);
    }

    @EventHandler
    public void onInventory(InventoryDragEvent e) {
        PaperGuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        AbstractGui<Gui> gui = (AbstractGui<Gui>) holder.gui();

        GuiDragEvent<Gui> event = new GuiDragEvent<>(e, gui, PaperGuiApi.get().user(e.getWhoClicked()), e.getRawSlots());
        gui.onDrag(event);
        if (event.isCancelled()) e.setCancelled(true);
    }
}
