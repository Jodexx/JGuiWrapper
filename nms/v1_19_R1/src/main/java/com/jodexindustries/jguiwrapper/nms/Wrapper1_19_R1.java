package com.jodexindustries.jguiwrapper.nms;

import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftContainer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class Wrapper1_19_R1 implements NMSWrapper {

    @Override
    public boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title, boolean refreshData) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();

        MenuType<?> menuType = type != null ? getNotchInventoryType(type, size) : sp.containerMenu.getType();
        net.minecraft.network.chat.Component menuTitle = title != null ? PaperAdventure.asVanilla(title) : sp.containerMenu.getTitle();

        ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(sp.containerMenu.containerId, menuType, menuTitle);
        sp.connection.send(packet);
        if (refreshData) sp.containerMenu.sendAllDataToRemote();
        return true;
    }

    @Override
    public InventoryView openInventory(HumanEntity player, @NotNull Inventory inventory, @NotNull InventoryType type, int size, Component title) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        MenuType<?> menuType = getNotchInventoryType(type, size);

        return openCustomInventory(inventory, sp, menuType, title);
    }

    private static InventoryView openCustomInventory(Inventory inventory, ServerPlayer player, MenuType<?> windowType, Component title) {
        AbstractContainerMenu container = new CraftContainer(inventory, player, player.nextContainerCounter());
        AbstractContainerMenu containerMenu = CraftEventFactory.callInventoryOpenEvent(player, container);
        if (containerMenu != null) {

            if (!player.isImmobile()) {
                player.connection.send(new ClientboundOpenScreenPacket(containerMenu.containerId, windowType, PaperAdventure.asVanilla(title)));
            }

            player.containerMenu = containerMenu;
            player.initMenu(containerMenu);

            return containerMenu.getBukkitView();
        }

        return null;
    }

    public static MenuType<?> getNotchInventoryType(InventoryType type, int size) {
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
            case SMITHING -> MenuType.SMITHING;
            case CREATIVE, CRAFTING, MERCHANT ->
                    throw new IllegalArgumentException("Can't open a " + type + " inventory!");
            default -> MenuType.GENERIC_9x3;
        };
    }
}
