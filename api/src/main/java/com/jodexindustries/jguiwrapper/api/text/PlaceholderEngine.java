package com.jodexindustries.jguiwrapper.api.text;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.user.User;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings({"unused"})
public interface PlaceholderEngine {

    void register(@NotNull String placeholder, @NotNull Function<User, @NotNull String> resolver);

    void register(@NotNull String placeholder, @NotNull String resolver);

    void register(@NotNull String placeholder, @NotNull Object resolver);

    void registerRegex(@NotNull String pattern, @NotNull BiFunction<@NotNull String, @Nullable User, @NotNull String> resolver);

    void registerRegex(@NotNull Pattern pattern, @NotNull BiFunction<@NotNull String, @Nullable User, @NotNull String> resolver);

    void addAll(@NotNull PlaceholderEngine placeholderEngine);

    @NotNull List<Component> process(@NotNull List<Component> input, @Nullable User user);

    @NotNull Component process(@NotNull Component input, @Nullable User user);

    /**
     * Creates a new {@link PlaceholderEngine}.
     *
     * @return a new engine instance
     */
    @NotNull
    static PlaceholderEngine of() {
        return GuiApi.get().createPlaceholderEngine();
    }
}
