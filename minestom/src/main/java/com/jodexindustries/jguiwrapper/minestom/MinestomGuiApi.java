package com.jodexindustries.jguiwrapper.minestom;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiType;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiFactory;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.common.factory.RegistryGuiFactory;
import com.jodexindustries.jguiwrapper.common.registry.GlobalRegistryImpl;
import com.jodexindustries.jguiwrapper.minestom.user.MinestomUser;
import com.jodexindustries.jguiwrapper.minestom.gui.GuiListener;
import com.jodexindustries.jguiwrapper.minestom.placeholder.PlaceholderEngineImpl;
import com.jodexindustries.jguiwrapper.minestom.gui.types.advanced.MinestomAdvancedGui;
import com.jodexindustries.jguiwrapper.minestom.gui.types.advanced.MinestomPaginatedGui;
import net.minestom.server.ServerProcess;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.logging.Logger;

public class MinestomGuiApi extends GuiApi {

    private final ServerProcess server;
    private static final GlobalRegistry REGISTRY = new GlobalRegistryImpl();
    private final RegistryGuiFactory registryGuiFactory = new RegistryGuiFactory();

    private MinestomGuiApi(Logger logger, ServerProcess server) {
        super(() -> logger);
        this.server = server;
        guiFactory().register(GuiType.ADVANCED, options -> new MinestomAdvancedGui(options.size(), options.title(), options.serializer()));
        guiFactory().register(GuiType.PAGINATED, options -> new MinestomPaginatedGui(options.size(), options.title(), options.serializer()));
    }

    /**
     * Gets the singleton instance of the API.
     *
     * @return the MinestomGuiApi instance
     * @throws IllegalStateException if the instance has not been set yet
     */
    @NotNull
    public static MinestomGuiApi get() {
        if (!(instance instanceof MinestomGuiApi api)) throw new IllegalStateException("GuiApi instance has not been set yet.");
        return api;
    }

    /**
     * Gets the singleton instance of the API, if it has been initialized.
     * <p>
     * Unlike {@link #get()}, this method does not throw an exception when the instance
     * is not yet set. Instead, it returns an {@link Optional} which will be empty
     * if the API has not been initialized.
     * </p>
     *
     * <pre>{@code
     * MinestomGuiApi.getOptional().ifPresent(api -> {
     *     // Safe access to the API
     *     ServerProcess process = api.server();
     * });
     * }</pre>
     *
     * @return an {@link Optional} containing the API instance if initialized,
     * or an empty optional if not
     */
    @NotNull
    public static Optional<MinestomGuiApi> getOptional() {
        if (!(instance instanceof MinestomGuiApi api)) return Optional.empty();
        return Optional.of(api);
    }

    public static void init(ServerProcess server) {
        init(server, Logger.getLogger("JGuiWrapper"));

        GuiListener.register(server.eventHandler());
    }

    public static void init(ServerProcess server, Logger logger) {
        if (instance != null) return;

        setInstance(new MinestomGuiApi(logger, server));
    }

    @Override
    public @NotNull PlaceholderEngine createPlaceholderEngine() {
        return new PlaceholderEngineImpl();
    }

    @Override
    public GuiFactory guiFactory() {
        return this.registryGuiFactory;
    }

    public @NotNull GlobalRegistry getRegistry() {
        return REGISTRY;
    }

    public @NotNull MinestomUser user(@NotNull Player player) {
        return MinestomUser.of(player);
    }

    public ServerProcess server() {
        return server;
    }
}
