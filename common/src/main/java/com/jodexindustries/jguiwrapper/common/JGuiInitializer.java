package com.jodexindustries.jguiwrapper.common;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.api.placeholder.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.common.placeholder.PlaceholderEngineImpl;
import com.jodexindustries.jguiwrapper.common.registry.GlobalRegistryImpl;
import com.jodexindustries.jguiwrapper.exception.JGuiWrapperVersionException;
import com.jodexindustries.jguiwrapper.common.gui.GuiListener;
import com.jodexindustries.jguiwrapper.nms.NMSMatcher;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public final class JGuiInitializer extends GuiApi {

    private static final GlobalRegistry REGISTRY = new GlobalRegistryImpl();
    private static NMSWrapper NMS_WRAPPER = NMSMatcher.EMPTY_WRAPPER;
    private static Plugin PLUGIN;
    private static boolean PAPI = false;
    private static SerializerType DEFAULT_SERIALIZER = SerializerType.LEGACY_AMPERSAND;

    private JGuiInitializer() {

    }

    public static void init(Plugin plugin) {
        if (JGuiInitializer.PLUGIN != null) return;

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
        JGuiInitializer.PLUGIN = plugin;

        PAPI = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");

        setInstance(new JGuiInitializer());

        try {
            NMS_WRAPPER = NMSMatcher.getWrapper(plugin);
        } catch (JGuiWrapperVersionException e) {
            plugin.getLogger().log(Level.WARNING, "NMSWrapper loading error: ", e);
        }
    }

    @Override
    public @NotNull GlobalRegistry getRegistry() {
        return REGISTRY;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return PLUGIN;
    }

    @Override
    public @NotNull NMSWrapper getNMSWrapper() {
        return NMS_WRAPPER;
    }

    @Override
    public @Nullable GuiHolder getOpenedGui(@NotNull Player player) {
        return GuiUtils.getHolder(player.getOpenInventory().getTopInventory());
    }

    @Override
    public @NotNull PlaceholderEngine createPlaceholderEngine() {
        return new PlaceholderEngineImpl();
    }

    @Override
    public boolean isPAPI() {
        return PAPI;
    }

    @Override
    public @NotNull SerializerType defaultSerializer() {
        return DEFAULT_SERIALIZER;
    }

    @Override
    public void defaultSerializer(@NotNull SerializerType serializerType) {
        DEFAULT_SERIALIZER = serializerType;
    }
}
