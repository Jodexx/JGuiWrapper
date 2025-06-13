package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.common.JGuiInitializer;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class JGuiPlugin extends JavaPlugin {

    private static final TestGui SIMPLE_GUI = new TestGui();

    private static final AdvancedGui ADVANCED_GUI = new AdvancedGui("&cAdvanced gui");

    @Override
    public void onEnable() {
        JGuiInitializer.init(this);
        Objects.requireNonNull(getCommand("jguiwrapper")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) return false;

//        SIMPLE_GUI.open(player);

        ADVANCED_GUI.registerItem("test", ItemWrapper.builder(Material.GOLD_BLOCK).build(), 0, (event) -> {
            event.setCancelled(true);
            GuiItemController controller = ADVANCED_GUI.getController(event.getRawSlot());
            controller.updateItem(itemWrapper -> {
                itemWrapper.displayName(Component.text(Math.random()));
                itemWrapper.update();
            });
        });

        ADVANCED_GUI.open(player);

        return true;
    }
}
