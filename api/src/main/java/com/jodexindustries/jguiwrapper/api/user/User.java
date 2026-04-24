package com.jodexindustries.jguiwrapper.api.user;

import org.jetbrains.annotations.NotNull;

public interface User {

    @NotNull
    <T> T as(@NotNull Class<T> type);
}
