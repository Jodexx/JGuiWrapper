package com.jodexindustries.jguiwrapper.common.placeholder;

import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.RegEx;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings({"unused"})
public class PlaceholderEngineImpl implements PlaceholderEngine<OfflinePlayer> {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([^%]+)%");

    private final Map<Pattern, @NotNull Function<@Nullable OfflinePlayer, String>> literalPlaceholders = new LinkedHashMap<>();
    private final Map<Pattern, @NotNull BiFunction<String, @Nullable OfflinePlayer, String>> regexPlaceholders = new LinkedHashMap<>();

    @Override
    public void register(@NotNull String placeholder, @NotNull Function<@Nullable OfflinePlayer, @NotNull String> resolver) {
        literalPlaceholders.put(Pattern.compile(placeholder, Pattern.LITERAL), resolver);
    }

    @Override
    public void register(@NotNull String placeholder, @NotNull String resolver) {
        register(placeholder, player -> resolver);
    }

    @Override
    public void register(@NotNull String placeholder, @NotNull Object resolver) {
        register(placeholder, String.valueOf(resolver));
    }

    @Override
    public void registerRegex(@NotNull @RegEx String pattern, @NotNull BiFunction<String, @Nullable OfflinePlayer, @NotNull String> resolver) {
        registerRegex(Pattern.compile(pattern), resolver);
    }

    @Override
    public void registerRegex(@NotNull Pattern pattern, @NotNull BiFunction<@NotNull String, @Nullable OfflinePlayer, @NotNull String> resolver) {
        regexPlaceholders.put(pattern, resolver);
    }

    @Override
    public void addAll(@NotNull PlaceholderEngine<OfflinePlayer> placeholderEngine) {
        PlaceholderEngineImpl engine = (PlaceholderEngineImpl) placeholderEngine;

        this.regexPlaceholders.putAll(engine.regexPlaceholders);
        this.literalPlaceholders.putAll(engine.literalPlaceholders);
    }

    @Override
    public @NotNull List<Component> process(@NotNull List<Component> input, @Nullable OfflinePlayer player) {
        return input.stream().map(component -> process(component, player)).collect(Collectors.toList());
    }

    @Override
    public @NotNull Component process(@NotNull Component input, @Nullable OfflinePlayer player) {
        Component current = input;

        for (Map.Entry<Pattern, Function<OfflinePlayer, String>> entry : literalPlaceholders.entrySet()) {
            Pattern pattern = entry.getKey();
            Function<OfflinePlayer, String> value = entry.getValue();

            try {
                current = current.replaceText(builder -> builder
                        .match(pattern)
                        .replacement(value.apply(player))
                );
            } catch (Exception e) {
                throw new RuntimeException("Error with replacing placeholder", e);
            }
        }

        for (Map.Entry<Pattern, BiFunction<String, OfflinePlayer, String>> entry : regexPlaceholders.entrySet()) {
            Pattern pattern = entry.getKey();
            BiFunction<String, OfflinePlayer, String> value = entry.getValue();

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

    private Component applyPapi(@NotNull Component component, @Nullable OfflinePlayer player) {
        if (!PaperGuiApi.get().isPAPI() || player == null) return component;

        return component.replaceText(TextReplacementConfig.builder()
                .match(PLACEHOLDER_PATTERN)
                .replacement(match -> {
                    String placeholder = match.content();
                    String parsed = PlaceholderAPI.setPlaceholders(player, placeholder);

                    match.content(parsed);
                    return match;
                })
                .build());
    }
}
