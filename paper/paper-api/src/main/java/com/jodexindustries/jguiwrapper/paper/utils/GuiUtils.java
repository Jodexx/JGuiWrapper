package com.jodexindustries.jguiwrapper.paper.utils;

import com.jodexindustries.jguiwrapper.paper.api.gui.PaperGuiHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

public class GuiUtils {

    @Nullable
    public static PaperGuiHolder getHolder(Inventory inventory) {
        if (inventory == null) return null;

        InventoryHolder holder = inventory.getHolder();
        if (holder == null) return null;

        return holder instanceof PaperGuiHolder ? ((PaperGuiHolder) holder) : null;
    }

}
