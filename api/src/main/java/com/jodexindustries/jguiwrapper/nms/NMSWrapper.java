package com.jodexindustries.jguiwrapper.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;

public interface NMSWrapper {

    void updateMenu(HumanEntity player, @Nullable InventoryType type, int size, Component title);
}
