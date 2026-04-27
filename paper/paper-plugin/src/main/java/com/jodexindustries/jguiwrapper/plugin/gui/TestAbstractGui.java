package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.paper.gui.AbstractGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class TestAbstractGui extends AbstractGui<TestAbstractGui> {

    public TestAbstractGui() {
        super("&cTest abstract gui");

        for (int i = 0; i < 5; i++) {
            ItemStack itemStack = new ItemStack(Material.DIAMOND);
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.displayName(Component.text(i));
            itemStack.setItemMeta(itemMeta);

            holder().setItem(i, itemStack);
        }
    }

    @Override
    public void onClick(@NotNull GuiClickEvent<TestAbstractGui> e) {
        e.user().sendMessage("You just clicked!");
    }
}
