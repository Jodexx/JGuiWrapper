package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface GuiHolder {

    @NotNull
    Gui gui();

    void setItem(@Range(from = 0L, to = 53L) int slot, @NotNull ItemWrapper item);

    void clear(@Range(from = 0L, to = 53L) int slot);
}
