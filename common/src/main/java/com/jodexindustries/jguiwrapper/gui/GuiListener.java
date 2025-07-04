package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.common.GuiUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.getGui().onClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getReason() == InventoryCloseEvent.Reason.PLUGIN) return;

        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.getGui().onClose(e);
    }

    @EventHandler
    public void onInventory(InventoryDragEvent e) {
        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.getGui().onDrag(e);
    }
}
