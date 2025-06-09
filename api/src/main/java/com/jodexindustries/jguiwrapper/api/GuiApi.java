package com.jodexindustries.jguiwrapper.api;

import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import org.bukkit.plugin.Plugin;

public abstract class GuiApi {

    private static volatile GuiApi instance = null;

    protected static synchronized void setInstance(GuiApi api) {
        if (GuiApi.instance != null) throw new IllegalStateException("GuiApi instance is already set.");
        GuiApi.instance = api;
    }

    public static GuiApi get() {
        if (GuiApi.instance == null) throw new IllegalStateException("GuiApi instance has not been set yet.");
        return instance;
    }

    public abstract Plugin getPlugin();

    public abstract NMSWrapper getNMSWrapper();
}
