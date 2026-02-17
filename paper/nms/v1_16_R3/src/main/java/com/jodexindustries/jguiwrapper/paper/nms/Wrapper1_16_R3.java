package com.jodexindustries.jguiwrapper.paper.nms;

import com.jodexindustries.jguiwrapper.paper.api.nms.NMSWrapper;
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

@SuppressWarnings("unused")
public class Wrapper1_16_R3 implements NMSWrapper {

    @Override
    public boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title, boolean refreshData) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer sp = craftPlayer.getHandle();

        Container containerMenu = sp.activeContainer;

        Containers<?> menuType = type != null ? getNotchInventoryType(type, size) : containerMenu.getType();

        IChatBaseComponent vanillaTitle = title == null ? containerMenu.getTitle() : PaperAdventure.asVanilla(title);

        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(containerMenu.windowId, menuType, vanillaTitle);

        sp.playerConnection.sendPacket(packet);
        if (refreshData) sp.updateInventory(containerMenu);
        return true;
    }

    @Override
    public InventoryView openInventory(@NotNull HumanEntity player, @NotNull Inventory inventory, @NotNull InventoryType type, int size, @NotNull Component title) {
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
        return switch (type) {
            case PLAYER, CHEST, ENDER_CHEST, BARREL -> switch (size) {
                case 9 -> Containers.GENERIC_9X1;
                case 18 -> Containers.GENERIC_9X2;
                case 27 -> Containers.GENERIC_9X3;
                case 36, 41 -> Containers.GENERIC_9X4;
                case 45 -> Containers.GENERIC_9X5;
                default -> Containers.GENERIC_9X6;
            };
            case WORKBENCH -> Containers.CRAFTING;
            case FURNACE -> Containers.FURNACE;
            case DISPENSER, DROPPER -> Containers.GENERIC_3X3;
            case ENCHANTING -> Containers.ENCHANTMENT;
            case BREWING -> Containers.BREWING_STAND;
            case BEACON -> Containers.BEACON;
            case ANVIL -> Containers.ANVIL;
            case HOPPER -> Containers.HOPPER;
            case SHULKER_BOX -> Containers.SHULKER_BOX;
            case BLAST_FURNACE -> Containers.BLAST_FURNACE;
            case LECTERN -> Containers.LECTERN;
            case SMOKER -> Containers.SMOKER;
            case LOOM -> Containers.LOOM;
            case CARTOGRAPHY -> Containers.CARTOGRAPHY_TABLE;
            case GRINDSTONE -> Containers.GRINDSTONE;
            case STONECUTTER -> Containers.STONECUTTER;
            case SMITHING -> Containers.SMITHING;
            case CREATIVE, CRAFTING, MERCHANT ->
                    throw new IllegalArgumentException("Can't open a " + type + " inventory!");
        };
    }

}
