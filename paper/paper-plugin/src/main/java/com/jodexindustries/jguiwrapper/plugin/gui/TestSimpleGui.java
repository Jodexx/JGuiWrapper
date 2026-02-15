package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class TestSimpleGui extends SimpleGui {

    private final SizeLooper looper = new SizeLooper();

    private int clicks;

    public TestSimpleGui() {
        super(3123, "&cExample");

        holder().getInventory().setItem(0, new ItemStack(Material.EMERALD));

        setClickHandlers((event, gui) -> {
            event.setCancelled(true);

            ClickType click = event.getClick();

            if (click.isRightClick())
                runTask(() -> updateMenu(event.getWhoClicked(), type(), looper.nextSize(), Component.text(clicks++), true));
            else
                event.getWhoClicked().sendMessage(String.valueOf(event.getRawSlot()));
        });

        onOpen(event -> event.getPlayer().sendMessage(event.getInventory().getType().defaultTitle()));

        onClose(event -> event.getPlayer().sendMessage(event.getReason().name()));

        onDrag(event -> event.getWhoClicked().sendMessage(event.getType().name()));
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
