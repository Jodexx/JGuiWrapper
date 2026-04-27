package com.jodexindustries.jguiwrapper.paper.gui.advanced.item;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused"})
public record HandlerContext(@NotNull LoadType loadType, @Nullable User user) {
}
