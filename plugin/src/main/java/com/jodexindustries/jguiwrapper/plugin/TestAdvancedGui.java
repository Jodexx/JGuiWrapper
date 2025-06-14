package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TestAdvancedGui extends AdvancedGui {

    private static final List<Material> VALID_MATERIALS = Arrays.stream(Material.values()).filter(Material::isItem).toList();

    private int clicks;

    public TestAdvancedGui() {
        super("&cAdvanced gui");

        registerItem("test", ItemWrapper.builder(Material.GOLD_BLOCK).build(), (event) -> {
            event.setCancelled(true);
            GuiItemController controller = getController(event.getRawSlot());
            controller.updateItem(itemWrapper -> {
                itemWrapper.displayName(Component.text(++clicks));
                itemWrapper.material(randomMaterial());
            });

            title("&cAdvanced gui clicked: &a" + clicks + " &ctimes");
            updateMenu();
        }, IntStream.range(0, size()).toArray());
    }

    private Material randomMaterial() {
        return VALID_MATERIALS.get(new Random().nextInt(VALID_MATERIALS.size()));
    }
}
