package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class TestAbstractGui extends AbstractGui {

    public TestAbstractGui() {
        super("&cTest abstract gui");

        Inventory inventory = holder().getInventory();

        for (int i = 0; i < 5; i++) {
            ItemStack itemStack = new ItemStack(Material.DIAMOND);
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.displayName(Component.text(i));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(i, itemStack);
        }
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent e) {
        HumanEntity player = e.getWhoClicked();

        player.sendMessage("You just clicked!");
    }
}
