package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import com.jodexindustries.jguiwrapper.gui.advanced.PaginatedAdvancedGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TestPaginatedAdvancedGui extends PaginatedAdvancedGui {

    @SuppressWarnings("unchecked")
    public TestPaginatedAdvancedGui() {
        super("&cAdvanced paginated gui");

        registerItem("prev_page", b -> b.slots(45)
                .defaultItem(ItemWrapper.builder(Material.ARROW).build())
                .defaultClickHandler((e, gui) -> {
                    e.setCancelled(true);
                    previousPage();
                }));

        registerItem("next_page", b -> b.slots(53)
                .defaultItem(ItemWrapper.builder(Material.ARROW).build())
                .defaultClickHandler((e, gui) -> {
                    e.setCancelled(true);
                    nextPage();
                }));

        int count = 10;

        for (int page = 1; page < 3; page++) {
            Consumer<GuiItemController.Builder>[] consumers = new Consumer[count];

            for (int i = 0; i < count; i++) {
                final int slot = i;
                ItemStack itemStack = new ItemStack(Material.DIAMOND, (i + 1) * page);

                Consumer<GuiItemController.Builder> item = (builder) -> builder.slots(slot).defaultItem(new ItemWrapper(itemStack));

                consumers[slot] = item;
            }

            addPage(consumers);
        }

        openPage(0); // draw the first page

    }
}
