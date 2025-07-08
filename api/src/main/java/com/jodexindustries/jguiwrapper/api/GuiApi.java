package com.jodexindustries.jguiwrapper.api;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GuiApi {

    private static volatile GuiApi instance = null;

    @ApiStatus.Internal
    protected static synchronized void setInstance(GuiApi api) {
        if (GuiApi.instance != null) throw new IllegalStateException("GuiApi instance is already set.");
        GuiApi.instance = api;
    }

    public static GuiApi get() {
        if (GuiApi.instance == null) throw new IllegalStateException("GuiApi instance has not been set yet.");
        return instance;
    }

    @NotNull
    public abstract GlobalRegistry getRegistry();

    @NotNull
    public abstract Plugin getPlugin();

    @NotNull
    public abstract NMSWrapper getNMSWrapper();

    @Nullable
    public abstract GuiHolder getOpenedGui(@NotNull Player player);
}
