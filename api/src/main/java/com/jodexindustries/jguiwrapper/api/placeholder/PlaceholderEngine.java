package com.jodexindustries.jguiwrapper.api.placeholder;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.RegEx;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings({"unused"})
public interface PlaceholderEngine {

    void register(@NotNull String placeholder, @NotNull Function<@Nullable OfflinePlayer, @NotNull String> resolver);

    void register(@NotNull String placeholder, @NotNull String resolver);

    void register(@NotNull String placeholder, @NotNull Object resolver);

    void registerRegex(@NotNull @RegEx String pattern, @NotNull BiFunction<@NotNull String, @Nullable OfflinePlayer, @NotNull String> resolver);

    void registerRegex(@NotNull Pattern pattern, @NotNull BiFunction<@NotNull String, @Nullable OfflinePlayer, @NotNull String> resolver);

    void addAll(@NotNull PlaceholderEngine placeholderEngine);

    @NotNull List<Component> process(@NotNull List<Component> input, @Nullable OfflinePlayer player);

    @NotNull Component process(@NotNull Component input, @Nullable OfflinePlayer player);

    /**
     * Creates a new PlaceholderEngine
     *
     * @return new PlaceholderEngine instance
     */
    @NotNull
    static PlaceholderEngine of() {
        return GuiApi.get().createPlaceholderEngine();
    }
}
