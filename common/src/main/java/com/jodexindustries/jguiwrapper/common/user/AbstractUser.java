package com.jodexindustries.jguiwrapper.common.user;

import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractUser<H> implements User {

    private final H handle;

    protected AbstractUser(@NotNull H handle) {
        this.handle = handle;
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(handle)) {
            return type.cast(handle);
        }
        throw new IllegalStateException("Unsupported handle type: " + type.getName());
    }
}
