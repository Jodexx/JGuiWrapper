package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.paper.api.item.PaperItemWrapper;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.GuiItemController;
import com.jodexindustries.jguiwrapper.plugin.JGuiPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.Random;

@SuppressWarnings("unused")
public class TestAdvancedGui extends AdvancedGui {

    private int clicks;

    public TestAdvancedGui() {
        super("&cAdvanced gui");

        registerLoader(JGuiPlugin.TEST_LOADER_KEY);

        onClose(event -> event.user().sendMessage("Closed"));

        onOpen(event -> {
            User user = event.user();
            user.sendMessage("Opened");

            loadData(user);

            // refresh item handlers
            for (GuiItemController controller : getControllers()) {
                controller.loadItemHandler(LoadType.ON_OPEN, user);
            }
        });

        registerItem("test", builder -> builder.slots(0, 1, 2, 3, 4, 5, 6, 7, 8)
                .defaultItem(PaperItemWrapper.builder(Material.GOLD_BLOCK).build())
                .defaultClickHandler((e, controller) -> {
                    e.setCancelled(true);
                    if (e.clickType() == GuiClickEvent.ClickType.DOUBLE_CLICK) return;

                    clicks++;

                    controller.updateItems(itemWrapper -> itemWrapper.meta().displayName(Component.text(clicks)));

                    title("&cAdvanced gui clicked: &a" + clicks + " &ctimes");
                    runTask(() -> updateMenu(true));
                }));

        registerItem("random", builder -> builder.slots(45)
                .defaultItem(
                        PaperItemWrapper.builder(Material.STRUCTURE_VOID)
                                .displayName("&#EC8C8CR&#F6A98Ca&#FFC58Bn&#FFE295d&#FFFF9Eo&#CDFF9Cm &#97CFF5b&#9DB5FAl&#A39AFFo&#C99DFFc&#EEA0FFk").build()
                )
                .defaultClickHandler((event, controller) -> {
                    event.setCancelled(true);
                    controller.updateItems(wrapper -> {
                        Material material = randomMaterial();
                        wrapper.material(material);
                        wrapper.meta().lore("&7Current material: &6" + material.name());
                    });
                })
        );

        registerItem("handled", builder -> builder.slots(52)
                .defaultItem(PaperItemWrapper.builder(Material.DIAMOND_BLOCK).build())
                .itemHandler(JGuiPlugin.TEST_HANDLER_KEY));

        registerItem("close", builder -> builder.slots(53)
                .defaultItem(PaperItemWrapper.builder(Material.BARRIER)
                        .displayName(defaultSerializer.deserialize("&cClose")).build())
                .defaultClickHandler((event, controller) -> {
                    event.setCancelled(true);

                    close(event.user());
                }));

    }

    private static Material randomMaterial() {
        Random random = new Random();

        Material[] values = Material.values();

        Material material = values[random.nextInt(values.length)];

        if (!material.isItem() || !material.isBlock() || material.isEmpty()) return randomMaterial();

        return material;
    }
}
