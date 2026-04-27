package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.PaperGuiBase;
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

        setClickHandlers((event, gui) -> {
            event.setCancelled(true);

            GuiClickEvent.ClickType click = event.clickType();

            if (click.isRightClick())
                runTask(() -> {
                    // unwrap
                    Player player = event.user().as(Player.class);
                    updateMenu(player, type(), looper.nextSize(), Component.text(clicks++), true);
                });
            else
                event.user().sendMessage(String.valueOf(event.rawSlot()));
        });

        onOpen(event -> {
            // unwrap
            InventoryOpenEvent openEvent = event.as(InventoryOpenEvent.class);
            event.user().sendMessage(openEvent.getInventory().getType().defaultTitle());
        });

        onClose(event -> {
            InventoryCloseEvent closeEvent = event.as(InventoryCloseEvent.class);
            event.user().sendMessage(closeEvent.getReason().name());
        });

        onDrag(event -> {
            InventoryDragEvent dragEvent = event.as(InventoryDragEvent.class);
            event.user().sendMessage(dragEvent.getType().name());
        });
    }

    
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
