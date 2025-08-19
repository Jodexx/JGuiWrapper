package com.jodexindustries.jguiwrapper.common.placeholder;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.placeholder.PlaceholderEngine;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.OfflinePlayer;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.RegEx;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"unused"})
public class PlaceholderEngineImpl implements PlaceholderEngine {

    private final Map<String, Function<@Nullable OfflinePlayer, String>> placeholders = new LinkedHashMap<>();
    private final Map<String, BiFunction<String, @Nullable OfflinePlayer, String>> regexPlaceholders = new LinkedHashMap<>();

    @Override
    public void register(@NotNull String placeholder, Function<@Nullable OfflinePlayer, @NotNull String> resolver) {
        placeholders.put(placeholder, resolver);
    }

    @Override
    public void register(@NotNull String placeholder, @NotNull String resolver) {
        placeholders.put(placeholder, player -> resolver);
    }

    @Override
    public void register(@NotNull String placeholder, @NotNull Object resolver) {
        register(placeholder, String.valueOf(resolver));
    }

    @Override
    public void registerRegex(@NotNull @RegEx String pattern, BiFunction<String, @Nullable OfflinePlayer, @NotNull String> resolver) {
        regexPlaceholders.put(pattern, resolver);
    }

    @Override
    public void addAll(@NotNull PlaceholderEngine placeholderEngine) {
        PlaceholderEngineImpl engine = (PlaceholderEngineImpl) placeholderEngine;

        this.regexPlaceholders.putAll(engine.regexPlaceholders);
        this.placeholders.putAll(engine.placeholders);
    }

    @Override
    public @NotNull List<Component> process(@NotNull List<Component> input, @Nullable OfflinePlayer player) {
        return input.stream().map(component -> process(component, player)).collect(Collectors.toList());
    }

    @Override
    public @NotNull Component process(@NotNull Component input, @Nullable OfflinePlayer player) {
        Component current = input;

        for (Map.Entry<String, Function<OfflinePlayer, String>> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            Function<OfflinePlayer, String> value = entry.getValue();

            try {
                current = current.replaceText(builder -> builder
                        .matchLiteral(key)
                        .replacement(value.apply(player))
                );
            } catch (Exception e) {
                throw new RuntimeException("Error with replacing placeholder", e);
            }
        }

        for (Map.Entry<String, BiFunction<String, @Nullable OfflinePlayer, String>> entry : regexPlaceholders.entrySet()) {
            @RegExp String pattern = entry.getKey();
            BiFunction<String, @Nullable OfflinePlayer, String> value = entry.getValue();

            current = current.replaceText(TextReplacementConfig.builder()
                    .match(pattern)
                    .replacement(match -> {
                        match.content(value.apply(match.content(), player));
                        return match;
                    })
                    .build());
        }

        current = applyPapi(current, player);

        return current;
    }

    private Component applyPapi(Component component, OfflinePlayer player) {
        if (!GuiApi.get().isPAPI() || player == null) return component;

        return component.replaceText(TextReplacementConfig.builder()
                .match("%([^%]+)%")
                .replacement(match -> {
                    String placeholder = match.content();
                    String parsed = PlaceholderAPI.setPlaceholders(player, placeholder);

                    match.content(parsed);
                    return match;
                })
                .build());
    }
}
