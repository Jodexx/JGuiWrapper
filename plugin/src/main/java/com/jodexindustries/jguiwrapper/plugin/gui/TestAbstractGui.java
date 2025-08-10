package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.common.JGuiInitializer;
import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public class TestAbstractGui extends AbstractGui {

    private final Logger logger = JGuiInitializer.get().getPlugin().getLogger();

    public TestAbstractGui() {
        super("&cTest abstract gui");
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();

        logger.info("Inventory: " + event.getInventory().getType());
        logger.info("Clicked inventory: " + event.getClickedInventory().getType());
        logger.info("Action: " + event.getAction());
        logger.info("Slot: " + event.getSlot());
        logger.info("Raw slot: " + event.getRawSlot());
        logger.info("--------");
    }
}
