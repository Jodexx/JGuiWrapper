package com.jodexindustries.jguiwrapper.common.user;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperUser extends AbstractUser<OfflinePlayer> {

    private PaperUser(@NotNull OfflinePlayer handle) {
        super(handle);
    }

    public static @NotNull PaperUser of(@NotNull Player player) {
        return new PaperUser(player);
    }

    public static @NotNull PaperUser of(@NotNull OfflinePlayer player) {
        return new PaperUser(player);
    }
}
