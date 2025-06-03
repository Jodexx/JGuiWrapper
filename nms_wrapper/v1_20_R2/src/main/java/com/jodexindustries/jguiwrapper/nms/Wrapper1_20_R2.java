package com.jodexindustries.jguiwrapper.nms;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;

public class Wrapper1_20_R2 implements NMSWrapper {

    @Override
    public void updateTitle(HumanEntity player, Component title) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();

        ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(sp.containerMenu.containerId, sp.containerMenu.getType(), PaperAdventure.asVanilla(title));
        sp.connection.send(packet);
    }
}
