package com.jodexindustries.jguiwrapper.api.gui;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface GuiHolder {

    @NotNull
    Component title();

    @Range(from = 0L, to = 54L)
    int size();
}
