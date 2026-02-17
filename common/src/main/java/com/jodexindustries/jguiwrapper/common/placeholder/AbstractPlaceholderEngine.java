package com.jodexindustries.jguiwrapper.common.placeholder;

import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractPlaceholderEngine<C> implements PlaceholderEngine<C> {

    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([^%]+)%");

    private final Map<Pattern, @NotNull Function<@Nullable C, String>> literalPlaceholders = new LinkedHashMap<>();
    private final Map<Pattern, @NotNull BiFunction<String, @Nullable C, String>> regexPlaceholders = new LinkedHashMap<>();

    @Override
    public void register(@NotNull String placeholder, @NotNull Function<@Nullable C, @NotNull String> resolver) {
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
    public void registerRegex(@NotNull String pattern, @NotNull BiFunction<String, @Nullable C, @NotNull String> resolver) {
        registerRegex(Pattern.compile(pattern), resolver);
    }

    @Override
    public void registerRegex(@NotNull Pattern pattern, @NotNull BiFunction<@NotNull String, @Nullable C, @NotNull String> resolver) {
        regexPlaceholders.put(pattern, resolver);
    }

    @Override
    public void addAll(@NotNull PlaceholderEngine<C> placeholderEngine) {
        AbstractPlaceholderEngine<C> engine = (AbstractPlaceholderEngine<C>) placeholderEngine;

        this.regexPlaceholders.putAll(engine.regexPlaceholders);
        this.literalPlaceholders.putAll(engine.literalPlaceholders);
    }

    @Override
    public @NotNull List<Component> process(@NotNull List<Component> input, @Nullable C player) {
        return input.stream().map(component -> process(component, player)).collect(Collectors.toList());
    }

    @Override
    public @NotNull Component process(@NotNull Component input, @Nullable C player) {
        Component current = input;

        for (Map.Entry<Pattern, Function<C, String>> entry : literalPlaceholders.entrySet()) {
            Pattern pattern = entry.getKey();
            Function<C, String> value = entry.getValue();

            try {
                current = current.replaceText(builder -> builder
                        .match(pattern)
                        .replacement(value.apply(player))
                );
            } catch (Exception e) {
                throw new RuntimeException("Error with replacing placeholder", e);
            }
        }

        for (Map.Entry<Pattern, BiFunction<String, C, String>> entry : regexPlaceholders.entrySet()) {
            Pattern pattern = entry.getKey();
            BiFunction<String, C, String> value = entry.getValue();

            current = current.replaceText(TextReplacementConfig.builder()
                    .match(pattern)
                    .replacement(match -> {
                        match.content(value.apply(match.content(), player));
                        return match;
                    })
                    .build());
        }

        return process0(current, player);
    }

    public abstract @NotNull Component process0(@NotNull Component input, @Nullable C player);
}
