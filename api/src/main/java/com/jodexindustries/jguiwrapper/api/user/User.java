package com.jodexindustries.jguiwrapper.api.user;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface User {

    void sendMessage(@NotNull String message);

    void sendMessage(@NotNull Component component);

    @NotNull
    <T> T as(@NotNull Class<T> type);
}
