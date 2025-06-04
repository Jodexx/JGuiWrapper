package com.jodexindustries.jguiwrapper;

import com.jodexindustries.jguiwrapper.exception.JGuiWrapperVersionException;
import com.jodexindustries.jguiwrapper.gui.GuiListener;
import com.jodexindustries.jguiwrapper.nms.NMSMatcher;
import com.jodexindustries.jguiwrapper.nms.NMSWrapper;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class JGuiInitializer {

    private static NMSWrapper NMS_WRAPPER = NMSMatcher.EMPTY_WRAPPER;

    private static Plugin plugin;

    public static void init(Plugin plugin) {
        if(JGuiInitializer.plugin != null) return;

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
        JGuiInitializer.plugin = plugin;

        try {
            NMS_WRAPPER = NMSMatcher.getWrapper(plugin);
        } catch (JGuiWrapperVersionException e) {
            plugin.getLogger().log(Level.WARNING, "NMSWrapper loading error: ", e);
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static NMSWrapper getNmsWrapper() {
        return NMS_WRAPPER;
    }
}
