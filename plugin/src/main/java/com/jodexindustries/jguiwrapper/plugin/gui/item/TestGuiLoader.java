package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.api.gui.GuiDataLoader;
import org.bukkit.entity.HumanEntity;

@SuppressWarnings("unused")
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
