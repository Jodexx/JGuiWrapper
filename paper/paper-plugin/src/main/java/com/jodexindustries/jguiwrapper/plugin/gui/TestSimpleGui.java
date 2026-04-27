package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.paper.gui.PaperGuiBase;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class TestSimpleGui extends PaperGuiBase<TestSimpleGui> {

    private final SizeLooper looper = new SizeLooper();

    private int clicks;

    public TestSimpleGui() {
        super(3123, "&cExample");

        holder().setItem(0, new ItemStack(Material.EMERALD));

        setClickHandlers((e, gui) -> {
            e.setCancelled(true);

            GuiClickEvent.ClickType click = e.clickType();

            if (click.isRightClick())
                runTask(() -> updateMenu(e.user().as(Player.class), type(), looper.nextSize(), Component.text(clicks++), true));
            else
                e.user().sendMessage(String.valueOf(e.rawSlot()));
        });

        onOpen(event -> event.user().sendMessage(event.as(InventoryOpenEvent.class).getInventory().getType().defaultTitle()));

        onClose(event -> event.user().sendMessage(event.as(InventoryCloseEvent.class).getReason().name()));

        onDrag(event -> event.user().sendMessage(event.as(InventoryDragEvent.class).getType().name()));
    }

    @SuppressWarnings("unused")
    private static class SizeLooper {
        private int currentSize = 54;
        private boolean increasing = true;

        public int nextSize() {
            if (increasing) {
                currentSize += 9;
                if (currentSize > 54) {
                    currentSize = 45;
                    increasing = false;
                }
            } else {
                currentSize -= 9;
                if (currentSize < 9) {
                    currentSize = 18;
                    increasing = true;
                }
            }
            return currentSize;
        }

    }

}
