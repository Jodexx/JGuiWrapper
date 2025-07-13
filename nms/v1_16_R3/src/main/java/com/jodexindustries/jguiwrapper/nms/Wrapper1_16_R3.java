package com.jodexindustries.jguiwrapper.nms;

import com.jodexindustries.jguiwrapper.accessors.*;
import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.nms.NMSUtils;
import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Wrapper1_16_R3 implements NMSWrapper {

    private final NMSUtils NMS_UTILS = GuiApi.get().getNMSUtils();

    @Override
    public boolean updateMenu(HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        Object sp = NMS_UTILS.getServerPlayer(player);
        Object containerMenu = NMS_UTILS.getField(PlayerAccessor.FIELD_CONTAINER_MENU.get(), sp);
        Object menuType = type != null ? getNotchInventoryType(type, size) : NMS_UTILS.invokeInstance(AbstractContainerMenuAccessor.METHOD_GET_TYPE.get(), containerMenu);

        int containerId = (int) NMS_UTILS.getField(AbstractContainerMenuAccessor.FIELD_CONTAINER_ID.get(), containerMenu);

        Object packet = NMS_UTILS.newInstance(ClientboundOpenScreenPacketAccessor.CONSTRUCTOR_0.get(), containerId, menuType, title);

        Object connection = NMS_UTILS.getField(ServerPlayerAccessor.FIELD_CONNECTION.get(), sp); // ServerGamePacketListenerImpl
        NMS_UTILS.invokeInstance(ServerGamePacketListenerImplAccessor.METHOD_SEND.get(), connection, packet);
        return true;
    }

    @Override
    public InventoryView openInventory(HumanEntity player, @NotNull Inventory inventory, @NotNull InventoryType type, int size, Component title) {
//        ServerPlayer sp = ((CraftPlayer) player).getHandle();
//        MenuType<?> menuType = getNotchInventoryType(type, size);
//
//        return openCustomInventory(inventory, sp, menuType, title);
        return null;
    }

//    private static InventoryView openCustomInventory(Inventory inventory, ServerPlayer player, MenuType<?> windowType, Component title) {
//        AbstractContainerMenu container = new CraftContainer(inventory, player, player.nextContainerCounter());
//        AbstractContainerMenu containerMenu = CraftEventFactory.callInventoryOpenEvent(player, container);
//        if (containerMenu != null) {
//
//            if (!player.isImmobile()) {
//                player.connection.send(new ClientboundOpenScreenPacket(containerMenu.containerId, windowType, PaperAdventure.asVanilla(title)));
//            }
//
//            player.containerMenu = containerMenu;
//            player.initMenu(containerMenu);
//
//            return containerMenu.getBukkitView();
//        }
//
//        return null;
//    }

    public Object getNotchInventoryType(InventoryType type, int size) {
        switch (type) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                switch (size) {
                    case 9:
                        return MenuTypeAccessor.FIELD_GENERIC_9X1.get();
                    case 18:
                        return MenuTypeAccessor.FIELD_GENERIC_9X2.get();
                    case 27:
                        return MenuTypeAccessor.FIELD_GENERIC_9X3.get();
                    case 36:
                    case 41:
                        return MenuTypeAccessor.FIELD_GENERIC_9X4.get();
                    case 45:
                        return MenuTypeAccessor.FIELD_GENERIC_9X5.get();
                    default:
                        return MenuTypeAccessor.FIELD_GENERIC_9X6.get();
                }
            case WORKBENCH:
                return MenuTypeAccessor.FIELD_CRAFTING.get();
            case FURNACE:
                return MenuTypeAccessor.FIELD_FURNACE.get();
            case DISPENSER:
            case DROPPER:
                return MenuTypeAccessor.FIELD_GENERIC_3X3.get();
            case ENCHANTING:
                return MenuTypeAccessor.FIELD_ENCHANTMENT.get();
            case BREWING:
                return MenuTypeAccessor.FIELD_BREWING_STAND.get();
            case BEACON:
                return MenuTypeAccessor.FIELD_BEACON.get();
            case ANVIL:
                return MenuTypeAccessor.FIELD_ANVIL.get();
            case HOPPER:
                return MenuTypeAccessor.FIELD_HOPPER.get();
            case SHULKER_BOX:
                return MenuTypeAccessor.FIELD_SHULKER_BOX.get();
            case BLAST_FURNACE:
                return MenuTypeAccessor.FIELD_BLAST_FURNACE.get();
            case LECTERN:
                return MenuTypeAccessor.FIELD_LECTERN.get();
            case SMOKER:
                return MenuTypeAccessor.FIELD_SMOKER.get();
            case LOOM:
                return MenuTypeAccessor.FIELD_LOOM.get();
            case CARTOGRAPHY:
                return MenuTypeAccessor.FIELD_CARTOGRAPHY_TABLE.get();
            case GRINDSTONE:
                return MenuTypeAccessor.FIELD_GRINDSTONE.get();
            case STONECUTTER:
                return MenuTypeAccessor.FIELD_STONECUTTER.get();
            case SMITHING:
                return MenuTypeAccessor.FIELD_SMITHING.get();
            case CREATIVE:
            case CRAFTING:
            case MERCHANT:
                throw new IllegalArgumentException("Can't open a " + type + " inventory!");
            default:
                return MenuTypeAccessor.FIELD_GENERIC_9X3.get();
        }
    }

}
