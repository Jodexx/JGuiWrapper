package com.jodexindustries.jguiwrapper.minestom.placeholder;

import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.common.placeholder.AbstractPlaceholderEngine;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderEngineImpl extends AbstractPlaceholderEngine {

    @Override
    public @NotNull Component process0(@NotNull Component input, @Nullable User player) {
        return input;
    }

}
