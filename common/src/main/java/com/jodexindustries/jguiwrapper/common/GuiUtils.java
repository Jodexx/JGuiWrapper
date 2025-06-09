package com.jodexindustries.jguiwrapper.common;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

public class GuiUtils {

    @Nullable
    public static GuiHolder getHolder(Inventory inventory) {
        if (inventory == null) return null;

        InventoryHolder holder = inventory.getHolder();
        if (holder == null) return null;

        return holder instanceof GuiHolder guiHolder ? guiHolder : null;
    }
}
