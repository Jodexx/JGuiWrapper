package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import com.jodexindustries.jguiwrapper.gui.advanced.PaginatedAdvancedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@SuppressWarnings({"unused", "unchecked"})
public class TestPaginatedAdvancedGui extends PaginatedAdvancedGui {

    public TestPaginatedAdvancedGui() {
        super(54, generateTitle(0, 5));

        registerItem("prev_page", b -> b.slots(45)
                .defaultItem(ItemWrapper.builder(Material.ARROW).build())
                .defaultClickHandler((e, c) -> {
                    e.setCancelled(true);
                    if (previousPage()) {
                        title(generateTitle());
                        runTask(() -> updateMenu(true));
                    }
                }));

        registerItem("next_page", b -> b.slots(53)
                .defaultItem(ItemWrapper.builder(Material.ARROW).build())
                .defaultClickHandler((e, c) -> {
                    e.setCancelled(true);
                    if (nextPage()) {
                        title(generateTitle());
                        runTask(() -> updateMenu(true));
                    }
                }));

        int pages = 5;
        int itemsPerPage = 45;

        for (int j = 0; j < pages; j++) {
            final int page = j;

            Consumer<GuiItemController.Builder>[] consumers = new Consumer[itemsPerPage];

            for (int i = 0; i < itemsPerPage; i++) {
                final int slot = i;

                ItemStack itemStack = new ItemStack(Material.DIAMOND, Math.min((i + 1), 64));

                Consumer<GuiItemController.Builder> item = (b) -> b.slots(slot)
                        .defaultItem(new ItemWrapper(itemStack))
                        .defaultClickHandler((e, c) -> {
                            e.setCancelled(true);
                            e.getWhoClicked().sendMessage("Page: " + page + " Item: " + slot);
                        });

                consumers[slot] = item;
            }

            addPage(consumers);
        }

        openPage(0); // draw the first page

    }

    private Component generateTitle() {
        return generateTitle(currentPage(), pages());
    }

    private static Component generateTitle(int currentPage, int max) {
        return SerializerType.LEGACY_AMPERSAND.deserialize("Page: &c" + (currentPage + 1) + "&0/&c" + max);
    }
}
