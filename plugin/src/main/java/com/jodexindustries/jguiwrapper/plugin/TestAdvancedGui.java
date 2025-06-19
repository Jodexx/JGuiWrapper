package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.stream.IntStream;

public class TestAdvancedGui extends AdvancedGui {

    private int clicks;

    public TestAdvancedGui() {
        super("&cAdvanced gui");


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


    }


}
