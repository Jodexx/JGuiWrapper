package com.jodexindustries.jguiwrapper.common;

import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.paper.utils.GuiUtils;
import com.jodexindustries.jguiwrapper.paper.api.gui.PaperGuiHolder;
import com.jodexindustries.jguiwrapper.paper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.paper.api.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.common.placeholder.PlaceholderEngineImpl;
import com.jodexindustries.jguiwrapper.common.registry.GlobalRegistryImpl;
import com.jodexindustries.jguiwrapper.paper.exception.JGuiWrapperVersionException;
import com.jodexindustries.jguiwrapper.common.gui.GuiListener;
import com.jodexindustries.jguiwrapper.nms.NMSMatcher;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

@SuppressWarnings("unused")
public final class PaperGuiApiImpl extends PaperGuiApi {

    private static final GlobalRegistry REGISTRY = new GlobalRegistryImpl();
    private static NMSWrapper NMS_WRAPPER;
    private static final NMSWrapper EMPTY_WRAPPER = new NMSWrapper() {
        @Override
        public boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, Component title, boolean refreshData) {
            return false;
        }

        @Override
        public InventoryView openInventory(@NotNull HumanEntity player, @NotNull Inventory inventory, @NotNull InventoryType type, int size, @NotNull Component title) {
            return null;
        }
    };

    private static boolean PAPI;
    private static SerializerType DEFAULT_SERIALIZER = SerializerType.LEGACY_AMPERSAND;

    private PaperGuiApiImpl(Plugin plugin) {
        super(plugin);
    }

    public static void init(Plugin plugin) {
        init(plugin, true);
    }

    public static void init(Plugin plugin, boolean log) {
        if (instance != null) return;

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);

        PAPI = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");

        setInstance(new PaperGuiApiImpl(plugin));

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
    public @NotNull NMSWrapper getNMSWrapper() {
        return NMS_WRAPPER == null ? EMPTY_WRAPPER : NMS_WRAPPER;
    }

    @Override
    public @Nullable PaperGuiHolder getOpenedGui(@NotNull Player player) {
        return GuiUtils.getHolder(player.getOpenInventory().getTopInventory());
    }

    @Override
    public @NotNull PlaceholderEngine<OfflinePlayer> createPlaceholderEngine0() {
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
