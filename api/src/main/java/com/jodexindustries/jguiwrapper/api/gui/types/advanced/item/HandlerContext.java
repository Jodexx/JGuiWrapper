package com.jodexindustries.jguiwrapper.api.gui.types.advanced.item;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record HandlerContext(@NotNull LoadType loadType, @Nullable User user) {}

