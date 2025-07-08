package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class TestAbstractGui extends AbstractGui {

    public TestAbstractGui() {
        super("&cTest abstract gui");
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();

        whoClicked.sendMessage("Clicked slot: " + event.getRawSlot());
    }
}
