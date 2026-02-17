package com.jodexindustries.jguiwrapper.common.placeholder;

import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused"})
public class PlaceholderEngineImpl extends AbstractPlaceholderEngine<OfflinePlayer> {

    @Override
    public @NotNull Component process0(@NotNull Component input, @Nullable OfflinePlayer player) {
        if (!PaperGuiApi.get().isPAPI() || player == null) return input;

        return input.replaceText(TextReplacementConfig.builder()
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
