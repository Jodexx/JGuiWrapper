package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class TestGui extends AbstractGui {

    private final SizeLooper looper = new SizeLooper();

    private int clicks;

    public TestGui() {
        super(3123, "&cExample");
    }

    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        title(String.valueOf(clicks++));
        updateMenu(event.getWhoClicked(), holder().getInventory().getType(), looper.nextSize(), title());
    }

    @Override
    public void onDrag(@NotNull InventoryDragEvent event) {
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
