package com.jodexindustries.jguiwrapper.common;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.utils.GuiUtils;
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
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public final class JGuiInitializer extends GuiApi {

    private static final GlobalRegistry REGISTRY = new GlobalRegistryImpl();
    private static NMSWrapper NMS_WRAPPER;
    private static NMSWrapper EMPTY_WRAPPER;
    private static Plugin PLUGIN;
    private static boolean PAPI;
    private static SerializerType DEFAULT_SERIALIZER = SerializerType.LEGACY_AMPERSAND;

    private JGuiInitializer() {
        EMPTY_WRAPPER = new NMSWrapper() {
            @Override
            public boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, Component title, boolean refreshData) {
                return false;
            }

            @Override
            public InventoryView openInventory(HumanEntity player, @NotNull Inventory inventory, @NotNull InventoryType type, int size, Component title) {
                return null;
            }
        };
    }

    public static void init(Plugin plugin) {
        init(plugin, true);
    }

    public static void init(Plugin plugin, boolean log) {
        if (JGuiInitializer.PLUGIN != null) return;

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
        JGuiInitializer.PLUGIN = plugin;

        PAPI = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");

        setInstance(new JGuiInitializer());

        try {
            try {
                NMS_WRAPPER = NMSMatcher.getWrapper(plugin, log);
            } catch (JGuiWrapperVersionException e) {
                if (log) plugin.getLogger().log(Level.WARNING, "NMSWrapper loading error: ", e);
            }
        } catch (Throwable e) {
            if (log) plugin.getLogger().log(Level.INFO, "NMSWrapper is not included in JGuiWrapper");
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
        return NMS_WRAPPER == null ? EMPTY_WRAPPER : NMS_WRAPPER;
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
