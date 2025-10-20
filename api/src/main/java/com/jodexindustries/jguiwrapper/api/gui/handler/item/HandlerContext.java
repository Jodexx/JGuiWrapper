package com.jodexindustries.jguiwrapper.api.gui.handler.item;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused"})
public record HandlerContext(@NotNull LoadType loadType, @Nullable HumanEntity player) {
}
