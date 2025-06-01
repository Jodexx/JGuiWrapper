package com.jodexindustries.jguiwrapper.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;

public interface NMSWrapper {

    void updateTitle(HumanEntity player, Component title);
}
