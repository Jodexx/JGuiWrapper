package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.stream.IntStream;

public class TestAdvancedGui extends AdvancedGui {

    private int clicks;

    public TestAdvancedGui() {
        super("&cAdvanced gui");

        registerLoader(JGuiPlugin.TEST_LOADER_KEY);

        onClose(event -> {
            event.getPlayer().sendMessage("Closed");
        });

        onOpen(event -> {
            event.getPlayer().sendMessage("Opened");

            loadData(event.getPlayer());

            // refresh item handlers
            getControllers().forEach(GuiItemController::loadItemHandler);
        });

        registerItem("test", builder -> {
            builder.withSlots(IntStream.range(0, size() / 2).toArray())
                    .withDefaultItem(ItemWrapper.builder(Material.GOLD_BLOCK).build())
                    .withDefaultClickHandler((event, controller) -> {
                        event.setCancelled(true);

                        clicks++;

                        controller.updateAllItemWrappers(itemWrapper -> {
                            itemWrapper.displayName(Component.text(clicks));
                        });

                        title("&cAdvanced gui clicked: &a" + clicks + " &ctimes");
                        updateMenu();
                    });
        });

        registerItem("handled", builder -> {
            builder.withSlots(size() - 2)
                    .withDefaultItem(ItemWrapper.builder(Material.DIAMOND_BLOCK).build())
                    .withItemHandler(JGuiPlugin.TEST_HANDLER_KEY)
                    .build();
        });

        registerItem("close", builder -> {
            builder.withSlots(size() - 1)
                    .withDefaultItem(ItemWrapper.builder(Material.BARRIER)
                            .displayName(LEGACY_AMPERSAND.deserialize("&cClose")).build())
                    .withDefaultClickHandler((event, controller) -> {
                        event.setCancelled(true);

                        close(event.getWhoClicked());
                    });
        });

    }
}