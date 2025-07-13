package com.jodexindustries.jguiwrapper.nms;

import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftContainer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Wrapper1_16_R3 implements NMSWrapper {

    @Override
    public boolean updateMenu(HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer sp = craftPlayer.getHandle();

        Container containerMenu = sp.activeContainer;

        Containers<?> menuType = type != null ? getNotchInventoryType(type, size) : containerMenu.getType();

        IChatBaseComponent vanillaTitle = title == null ? containerMenu.getTitle() : PaperAdventure.asVanilla(title);

        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(containerMenu.windowId, menuType, vanillaTitle);

        sp.playerConnection.sendPacket(packet);
        return true;
    }

    @Override
    public InventoryView openInventory(HumanEntity player, @NotNull Inventory inventory, @NotNull InventoryType type, int size, Component title) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer sp = craftPlayer.getHandle();

        Containers<?> menuType = getNotchInventoryType(type, size);
        return openCustomInventory(inventory, sp, menuType, title);
    }

    private InventoryView openCustomInventory(Inventory inventory, EntityPlayer player, Containers<?> menuType, Component title) {
        Container container = new CraftContainer(inventory, player, player.nextContainerCounter());
        Container containerMenu = CraftEventFactory.callInventoryOpenEvent(player, container);
        if (containerMenu != null) {

            if (!player.isFrozen()) {
                PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(containerMenu.windowId, menuType, PaperAdventure.asVanilla(title));
                player.playerConnection.sendPacket(packet);
            }

            player.activeContainer = containerMenu;
            player.syncInventory();

            return containerMenu.getBukkitView();
        }

        return null;
    }

    public Containers<?> getNotchInventoryType(InventoryType type, int size) {
        switch (type) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                switch (size) {
                    case 9:
                        return Containers.GENERIC_9X1;
                    case 18:
                        return Containers.GENERIC_9X2;
                    case 27:
                        return Containers.GENERIC_9X3;
                    case 36:
                    case 41:
                        return Containers.GENERIC_9X4;
                    case 45:
                        return Containers.GENERIC_9X5;
                    default:
                        return Containers.GENERIC_9X6;
                }
            case WORKBENCH:
                return Containers.CRAFTING;
            case FURNACE:
                return Containers.FURNACE;
            case DISPENSER:
            case DROPPER:
                return Containers.GENERIC_3X3;
            case ENCHANTING:
                return Containers.ENCHANTMENT;
            case BREWING:
                return Containers.BREWING_STAND;
            case BEACON:
                return Containers.BEACON;
            case ANVIL:
                return Containers.ANVIL;
            case HOPPER:
                return Containers.HOPPER;
            case SHULKER_BOX:
                return Containers.SHULKER_BOX;
            case BLAST_FURNACE:
                return Containers.BLAST_FURNACE;
            case LECTERN:
                return Containers.LECTERN;
            case SMOKER:
                return Containers.SMOKER;
            case LOOM:
                return Containers.LOOM;
            case CARTOGRAPHY:
                return Containers.CARTOGRAPHY_TABLE;
            case GRINDSTONE:
                return Containers.GRINDSTONE;
            case STONECUTTER:
                return Containers.STONECUTTER;
            case SMITHING:
                return Containers.SMITHING;
            case CREATIVE:
            case CRAFTING:
            case MERCHANT:
                throw new IllegalArgumentException("Can't open a " + type + " inventory!");
            default:
                return Containers.GENERIC_9X3;
        }
    }

}
