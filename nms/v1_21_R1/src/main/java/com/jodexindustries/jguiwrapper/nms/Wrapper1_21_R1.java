package com.jodexindustries.jguiwrapper.nms;

import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;

public class Wrapper1_21_R1 implements NMSWrapper {

    @Override
    public void updateMenu(HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();

        MenuType<?> menuType = type != null ? getNotchInventoryType(type, size) : sp.containerMenu.getType();
        net.minecraft.network.chat.Component menuTitle = title != null ? PaperAdventure.asVanilla(title) : sp.containerMenu.getTitle();

        ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(sp.containerMenu.containerId, menuType, menuTitle);
        sp.connection.send(packet);
    }

    public static net.minecraft.world.inventory.MenuType<?> getNotchInventoryType(InventoryType type, int size) {
        return switch (type) {
            case PLAYER, CHEST, ENDER_CHEST, BARREL -> switch (size) {
                case 9 -> MenuType.GENERIC_9x1;
                case 18 -> MenuType.GENERIC_9x2;
                case 27 -> MenuType.GENERIC_9x3;
                case 36, 41 -> MenuType.GENERIC_9x4;
                case 45 -> MenuType.GENERIC_9x5;
                default -> MenuType.GENERIC_9x6;
            };
            case WORKBENCH -> MenuType.CRAFTING;
            case FURNACE -> MenuType.FURNACE;
            case DISPENSER, DROPPER -> MenuType.GENERIC_3x3;
            case ENCHANTING -> MenuType.ENCHANTMENT;
            case BREWING -> MenuType.BREWING_STAND;
            case BEACON -> MenuType.BEACON;
            case ANVIL -> MenuType.ANVIL;
            case HOPPER -> MenuType.HOPPER;
            case SHULKER_BOX -> MenuType.SHULKER_BOX;
            case BLAST_FURNACE -> MenuType.BLAST_FURNACE;
            case LECTERN -> MenuType.LECTERN;
            case SMOKER -> MenuType.SMOKER;
            case LOOM -> MenuType.LOOM;
            case CARTOGRAPHY -> MenuType.CARTOGRAPHY_TABLE;
            case GRINDSTONE -> MenuType.GRINDSTONE;
            case STONECUTTER -> MenuType.STONECUTTER;
            case SMITHING_NEW, SMITHING -> MenuType.SMITHING;
            case CREATIVE, CRAFTING, MERCHANT, DECORATED_POT, COMPOSTER, CHISELED_BOOKSHELF, JUKEBOX ->
                    throw new IllegalArgumentException("Can't open a " + type + " inventory!");
            case CRAFTER -> MenuType.CRAFTER_3x3;
        };
    }
}
