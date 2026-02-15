package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public interface GuiDataLoader {

    void load(@NotNull AdvancedGui gui, @NotNull HumanEntity player);
}
