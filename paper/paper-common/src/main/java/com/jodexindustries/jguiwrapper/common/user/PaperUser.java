package com.jodexindustries.jguiwrapper.common.user;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperUser extends AbstractUser<Player> {

    private PaperUser(@NotNull Player handle) {
        super(handle);
    }

    @Override
    public void sendMessage(@NotNull String message) {
        handle.sendMessage(message);
    }

    @Override
    public void sendMessage(@NotNull Component component) {
        handle.sendMessage(component);
    }

    public static @NotNull PaperUser of(@NotNull Player player) {
        return new PaperUser(player);
    }

    public static @NotNull PaperUser of(@NotNull HumanEntity player) {
        return new PaperUser((Player) player);
    }
}
