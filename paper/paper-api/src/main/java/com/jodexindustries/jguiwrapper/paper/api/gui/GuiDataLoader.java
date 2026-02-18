package com.jodexindustries.jguiwrapper.paper.api.gui;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public interface GuiDataLoader<T extends Gui> {

    void load(@NotNull T gui, @NotNull HumanEntity player);
}
