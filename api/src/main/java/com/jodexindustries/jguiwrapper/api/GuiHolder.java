package com.jodexindustries.jguiwrapper.api;

import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface GuiHolder extends InventoryHolder {

    @NotNull AbstractGui getGui();
}
