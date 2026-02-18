package com.jodexindustries.jguiwrapper.paper.api.gui.handler.item;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused"})
public record HandlerContext<P>(@NotNull LoadType loadType, @Nullable P player) {
}
