package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.common.JGuiInitializer;
import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public class TestAbstractGui extends AbstractGui {

    private final Logger logger = JGuiInitializer.get().getPlugin().getLogger();

    public TestAbstractGui() {
        super("&cTest abstract gui");
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent e) {
        HumanEntity whoClicked = e.getWhoClicked();

        Inventory clickedInventory = e.getClickedInventory();

        logger.info("Inventory: " + e.getInventory().getType());
        logger.info("Clicked inventory: " + (clickedInventory != null ? clickedInventory.getType() : "null"));
        logger.info("Action: " + e.getAction());
        logger.info("Slot: " + e.getSlot());
        logger.info("Raw slot: " + e.getRawSlot());
        logger.info("--------");
    }
}
