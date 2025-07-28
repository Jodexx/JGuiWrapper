package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import org.bukkit.entity.HumanEntity;

@SuppressWarnings({"unused"})
public interface GuiDataLoader {

    void load(AdvancedGui gui, HumanEntity player);
}
