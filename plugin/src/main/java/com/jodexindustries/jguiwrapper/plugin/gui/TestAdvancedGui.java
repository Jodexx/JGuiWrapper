package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import com.jodexindustries.jguiwrapper.plugin.JGuiPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class TestAdvancedGui extends AdvancedGui {

    private int clicks;

    public TestAdvancedGui() {
        super("&cAdvanced gui");

        registerLoader(JGuiPlugin.TEST_LOADER_KEY);

        // load item handlers
        for (GuiItemController controller : getControllers()) {
            controller.loadItemHandler(LoadType.ON_LOAD);
        }

        onClose(event -> event.getPlayer().sendMessage("Closed"));

        onOpen(event -> {
            HumanEntity player = event.getPlayer();
            player.sendMessage("Opened");

            loadData(player);

            // refresh item handlers
            for (GuiItemController controller : getControllers()) {
                controller.loadItemHandler(LoadType.ON_OPEN, player);
            }
        });

        registerItem("test", builder -> builder.slots(IntStream.range(0, size() / 2).toArray())
                .defaultItem(ItemWrapper.builder(Material.GOLD_BLOCK).build())
                .defaultClickHandler((event, controller) -> {
                    event.setCancelled(true);

                    clicks++;

                    controller.updateItems(itemWrapper -> itemWrapper.displayName(Component.text(clicks)));

                    title("&cAdvanced gui clicked: &a" + clicks + " &ctimes");
                    updateMenu();
                }));

        registerItem("handled", builder -> builder.slots(size() - 2)
                .defaultItem(ItemWrapper.builder(Material.DIAMOND_BLOCK).build())
                .itemHandler(JGuiPlugin.TEST_HANDLER_KEY)
                .build());

        registerItem("close", builder -> builder.slots(size() - 1)
                .defaultItem(ItemWrapper.builder(Material.BARRIER)
                        .displayName(LEGACY_AMPERSAND.deserialize("&cClose")).build())
                .defaultClickHandler((event, controller) -> {
                    event.setCancelled(true);

                    close(event.getWhoClicked());
                }));

    }
}