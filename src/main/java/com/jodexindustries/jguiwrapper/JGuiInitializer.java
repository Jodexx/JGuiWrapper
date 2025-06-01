package com.jodexindustries.jguiwrapper;

import com.jodexindustries.jguiwrapper.gui.GuiListener;
import org.bukkit.plugin.Plugin;

public class JGuiInitializer {

    private static Plugin plugin;

    public static void init(Plugin plugin) {
        if(JGuiInitializer.plugin != null) {
            plugin.getLogger().warning("JGuiWrapper already initialized!");
            return;
        }

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
        JGuiInitializer.plugin = plugin;

    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
