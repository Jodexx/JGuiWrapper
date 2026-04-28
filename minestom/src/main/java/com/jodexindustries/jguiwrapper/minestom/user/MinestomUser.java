package com.jodexindustries.jguiwrapper.minestom.user;

import com.jodexindustries.jguiwrapper.common.user.AbstractUser;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MinestomUser extends AbstractUser<Player> {

    private MinestomUser(@NotNull Player handle) {
        super(handle);
    }

    public static @NotNull MinestomUser of(@NotNull Player player) {
        return new MinestomUser(player);
    }

    @Override
    public void sendMessage(@NotNull String message) {
        handle.sendMessage(message);
    }

    @Override
    public void sendMessage(@NotNull Component component) {
        handle.sendMessage(component);
    }
}
