package com.jodexindustries.jguiwrapper.api.text;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings({"unused"})
public interface PlaceholderEngine<C> {

    void register(@NotNull String placeholder, @NotNull Function<@Nullable C, @NotNull String> resolver);

    void register(@NotNull String placeholder, @NotNull String resolver);

    void register(@NotNull String placeholder, @NotNull Object resolver);

    void registerRegex(@NotNull String pattern, @NotNull BiFunction<@NotNull String, @Nullable C, @NotNull String> resolver);

    void registerRegex(@NotNull Pattern pattern, @NotNull BiFunction<@NotNull String, @Nullable C, @NotNull String> resolver);

    void addAll(@NotNull PlaceholderEngine<C> placeholderEngine);

    @NotNull List<Component> process(@NotNull List<Component> input, @Nullable C player);

    @NotNull Component process(@NotNull Component input, @Nullable C player);

    /**
     * Creates a new {@link PlaceholderEngine}.
     *
     * @return a new engine instance
     */
    @NotNull
    static <C> PlaceholderEngine<C> of() {
        return GuiApi.get().createPlaceholderEngine();
    }

    /**
     * Creates a new {@link PlaceholderEngine} with the expected context type.
     * <p>
     * This overload delegates type validation to {@link GuiApi} and is useful when
     * you want an explicit runtime check for the context class used by resolvers.
     * </p>
     *
     * @param type expected context type used by this engine
     * @return new {@link PlaceholderEngine} instance
     * @throws IllegalStateException if the API provides an incompatible engine type
     */
    @NotNull
    static <C> PlaceholderEngine<C> of(Class<C> type) {
        return GuiApi.get().createPlaceholderEngine(type);
    }
}
