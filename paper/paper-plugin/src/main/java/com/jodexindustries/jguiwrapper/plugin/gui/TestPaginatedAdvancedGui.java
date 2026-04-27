package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.advanced.PaginatedAdvancedGui;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.advanced.PaperAdvancedGui;
import com.jodexindustries.jguiwrapper.paper.api.item.PaperItemWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestPaginatedAdvancedGui extends PaginatedAdvancedGui {

    public TestPaginatedAdvancedGui() {
        super(54, generateTitle(0, 5));

        registerItem("prev_page", b -> b.slots(45)
                .defaultItem(PaperItemWrapper.builder(Material.ARROW).build())
                .defaultClickHandler((e, c) -> {
                    e.setCancelled(true);
                    if (previousPage()) {
                        title(generateTitle());
                        runTask(() -> updateMenu(true));
                    }
                }));

        registerItem("next_page", b -> b.slots(53)
                .defaultItem(PaperItemWrapper.builder(Material.ARROW).build())
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


            List<Consumer<AdvancedGuiItemController.Builder<PaperAdvancedGui>>> consumers = new ArrayList<>(itemsPerPage);

            for (int i = 0; i < itemsPerPage; i++) {
                final int slot = i;

                ItemStack itemStack = new ItemStack(Material.DIAMOND, Math.min((i + 1), 64));

                Consumer<AdvancedGuiItemController.Builder<PaperAdvancedGui>> item = (b) -> b.slots(slot)
                        .defaultItem(new PaperItemWrapper(itemStack))
                        .defaultClickHandler((e, c) -> {
                            e.setCancelled(true);
                            e.user().sendMessage("Page: " + page + " Item: " + slot);
                        });

                consumers.add(item);
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
