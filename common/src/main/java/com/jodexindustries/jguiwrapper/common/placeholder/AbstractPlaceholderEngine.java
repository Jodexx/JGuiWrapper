package com.jodexindustries.jguiwrapper.common.placeholder;

import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.user.User;
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

public abstract class AbstractPlaceholderEngine implements PlaceholderEngine {

    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([^%]+)%");

    private final Map<Pattern, @NotNull Function<@Nullable User, String>> literalPlaceholders = new LinkedHashMap<>();
    private final Map<Pattern, @NotNull BiFunction<String, @Nullable User, String>> regexPlaceholders = new LinkedHashMap<>();

    @Override
    public void register(@NotNull String placeholder, @NotNull Function<@Nullable User, @NotNull String> resolver) {
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
    public void registerRegex(@NotNull String pattern, @NotNull BiFunction<String, @Nullable User, @NotNull String> resolver) {
        registerRegex(Pattern.compile(pattern), resolver);
    }

    @Override
    public void registerRegex(@NotNull Pattern pattern, @NotNull BiFunction<@NotNull String, @Nullable User, @NotNull String> resolver) {
        regexPlaceholders.put(pattern, resolver);
    }

    @Override
    public void addAll(@NotNull PlaceholderEngine placeholderEngine) {
        AbstractPlaceholderEngine engine = (AbstractPlaceholderEngine) placeholderEngine;

        this.regexPlaceholders.putAll(engine.regexPlaceholders);
        this.literalPlaceholders.putAll(engine.literalPlaceholders);
    }

    @Override
    public @NotNull List<Component> process(@NotNull List<Component> input, @Nullable User user) {
        return input.stream().map(component -> process(component, user)).collect(Collectors.toList());
    }

    @Override
    public @NotNull Component process(@NotNull Component input, @Nullable User user) {
        Component current = input;

        for (Map.Entry<Pattern, Function<User, String>> entry : literalPlaceholders.entrySet()) {
            Pattern pattern = entry.getKey();
            Function<User, String> value = entry.getValue();

            try {
                current = current.replaceText(builder -> builder
                        .match(pattern)
                        .replacement(value.apply(user))
                );
            } catch (Exception e) {
                throw new RuntimeException("Error with replacing placeholder", e);
            }
        }

        for (Map.Entry<Pattern, BiFunction<String, User, String>> entry : regexPlaceholders.entrySet()) {
            Pattern pattern = entry.getKey();
            BiFunction<String, User, String> value = entry.getValue();

            current = current.replaceText(TextReplacementConfig.builder()
                    .match(pattern)
                    .replacement(match -> {
                        match.content(value.apply(match.content(), user));
                        return match;
                    })
                    .build());
        }

        return process0(current, user);
    }

    public abstract @NotNull Component process0(@NotNull Component input, User player);
}
