package com.jodexindustries.jguiwrapper.minestom.utils;

import com.jodexindustries.jguiwrapper.minestom.gui.MinestomGuiHolder;
import net.minestom.server.inventory.AbstractInventory;

public class GuiUtils {

    public static MinestomGuiHolder getHolder(AbstractInventory inventory) {
        if (inventory == null || !inventory.hasTag(MinestomGuiHolder.GUI_HOLDER_TAG)) return null;

        return inventory.getTag(MinestomGuiHolder.GUI_HOLDER_TAG);
    }
}
