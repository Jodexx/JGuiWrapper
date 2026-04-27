package com.jodexindustries.jguiwrapper.common;

import com.jodexindustries.jguiwrapper.api.gui.factory.GuiFactory;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiType;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.common.factory.RegistryGuiFactory;
import com.jodexindustries.jguiwrapper.common.gui.GuiListener;
import com.jodexindustries.jguiwrapper.common.nms.JGuiWrapperVersionException;
import com.jodexindustries.jguiwrapper.common.nms.NMSMatcher;
import com.jodexindustries.jguiwrapper.common.placeholder.PlaceholderEngineImpl;
import com.jodexindustries.jguiwrapper.common.registry.GlobalRegistryImpl;
import com.jodexindustries.jguiwrapper.common.user.PaperUser;
import com.jodexindustries.jguiwrapper.common.utils.GuiUtils;
import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import com.jodexindustries.jguiwrapper.paper.api.gui.PaperGuiHolder;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.advanced.PaginatedAdvancedGui;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.advanced.PaperAdvancedGui;
import com.jodexindustries.jguiwrapper.paper.api.nms.NMSWrapper;
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

    private final RegistryGuiFactory registryGuiFactory = new RegistryGuiFactory();

    private PaperGuiApiImpl(Plugin plugin) {
        super(plugin);
        guiFactory().register(GuiType.ADVANCED, options -> new PaperAdvancedGui(options.size(), options.title()));
        guiFactory().register(GuiType.PAGINATED, options -> new PaginatedAdvancedGui(options.size(), options.title()));
    }

    public static void init(Plugin plugin) {
        init(plugin, true);
    }

    public static void init(Plugin plugin, boolean log) {
        if (instance != null) return;

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);

        PAPI = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

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
    public @NotNull PlaceholderEngine createPlaceholderEngine() {
        return new PlaceholderEngineImpl();
    }

    @Override
    public GuiFactory guiFactory() {
        return this.registryGuiFactory;
    }

    @Override
    public @NotNull User user(@NotNull Player player) {
        return PaperUser.of(player);
    }

    @Override
    public @NotNull User user(@NotNull HumanEntity player) {
        return PaperUser.of(player);
    }

    @Override
    public boolean isPAPI() {
        return PAPI;
    }
}
