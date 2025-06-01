package com.jodexindustries.jguiwrapper.nms;

import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;

public class Wrapper1_21_R4 implements NMSWrapper {

    @Override
    public void updateTitle(HumanEntity player, Component title) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();

        net.minecraft.network.chat.Component component = new AdventureComponent(title);

        ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(sp.containerMenu.containerId, sp.containerMenu.getType(), component);
        sp.connection.sendPacket(packet);
    }
}
