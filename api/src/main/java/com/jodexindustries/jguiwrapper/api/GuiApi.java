package com.jodexindustries.jguiwrapper.api;

import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.utils.ReflectionUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class GuiApi {

    protected static volatile GuiApi instance = null;

    protected final Module module;

    protected GuiApi(Module module) {
        this.module = module;
    }

    @ApiStatus.Internal
    protected static synchronized void setInstance(@NotNull GuiApi api) {
        if (GuiApi.instance != null) throw new IllegalStateException("GuiApi instance is already set.");
        GuiApi.instance = api;
    }

    /**
     * Gets the singleton instance of the API.
     *
     * @return the GuiApi instance
     * @throws IllegalStateException if the instance has not been set yet
     */
    @NotNull
    public static GuiApi get() {
        if (GuiApi.instance == null) throw new IllegalStateException("GuiApi instance has not been set yet.");
        return instance;
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
     * GuiApi.getOptional().ifPresent(api -> {
     *     // Safe access to the API
     *     Plugin plugin = api.getPlugin();
     * });
     * }</pre>
     *
     * @return an {@link Optional} containing the API instance if initialized,
     * or an empty optional if not
     */
    @NotNull
    public static Optional<? extends GuiApi> getOptional() {
        return Optional.ofNullable(instance);
    }

    /**
     * Gets the moduile instance associated with this API.
     *
     * @return the module instance
     */
    @NotNull
    public Module getModule() {
        return this.module;
    }

    /**
     * Returns the default {@link SerializerType} used by the API.
     * <p>
     * By default, this value is set to {@link SerializerType#LEGACY_AMPERSAND}, but it may be
     * changed at runtime using {@link #defaultSerializer(SerializerType)}.
     * </p>
     *
     * @return the default {@link SerializerType}, never {@code null}
     */
    @NotNull
    public abstract SerializerType defaultSerializer();

    /**
     * Sets the default {@link SerializerType} to be used by the API.
     * <p>
     * This will affect all future operations that rely on the default serializer,
     * unless explicitly overridden.
     * </p>
     *
     * @param serializerType the new default {@link SerializerType}, must not be {@code null}
     */
    public abstract void defaultSerializer(@NotNull SerializerType serializerType);

    /**
     * Creates a new {@link PlaceholderEngine}.
     *
     * @return a new engine instance
     */
    @SuppressWarnings("unchecked")
    @NotNull
    @ApiStatus.Experimental
    public final <C> PlaceholderEngine<C> createPlaceholderEngine() {
        return (PlaceholderEngine<C>) createPlaceholderEngine0();
    }

    /**
     * Creates a new {@link PlaceholderEngine} for the expected context type.
     *
     * @param type expected context type
     * @return a new engine instance
     * @throws IllegalStateException if the provided type is incompatible
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public final <C> PlaceholderEngine<C> createPlaceholderEngine(@NotNull Class<C> type) {
        PlaceholderEngine<?> engine = createPlaceholderEngine0();

        Class<?> genericClass = ReflectionUtils.getGenericClass(engine.getClass(), 0);

        if (!type.equals(genericClass)) {
            throw new IllegalStateException("Wrong context type");
        }

        return (PlaceholderEngine<C>) engine;
    }

    protected abstract PlaceholderEngine<?> createPlaceholderEngine0();
}
