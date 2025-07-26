package com.jodexindustries.jguiwrapper.api.gui.handler.item;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused"})
public class HandlerContext {

    private final LoadType loadType;
    private final HumanEntity player;

    public HandlerContext(@NotNull LoadType loadType, @Nullable HumanEntity player) {
        this.loadType = loadType;
        this.player = player;
    }

    @Nullable
    public HumanEntity player() {
        return player;
    }

    @NotNull
    public LoadType loadType() {
        return loadType;
    }
}
