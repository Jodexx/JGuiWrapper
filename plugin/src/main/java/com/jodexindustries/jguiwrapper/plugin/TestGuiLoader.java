package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.registry.GuiDataLoader;
import org.bukkit.entity.HumanEntity;

public class TestGuiLoader implements GuiDataLoader {

    private int openCount;

    @Override
    public void load(HumanEntity player) {
        openCount++;
    }

    public int getOpenCount() {
        return openCount;
    }
}
