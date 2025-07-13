package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.api.registry.GuiDataLoader;
import com.jodexindustries.jguiwrapper.common.JGuiInitializer;
import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import net.kyori.adventure.key.Key;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public final class JGuiPlugin extends JavaPlugin {

    public static final Key TEST_LOADER_KEY = Key.key("jguiwrapper", "test");

    @Override
    public void onEnable() {
        JGuiInitializer.init(this);

        GlobalRegistry registry = JGuiInitializer.get().getRegistry();
        registry.registerLoader(TEST_LOADER_KEY, new TestGuiLoader());

        Objects.requireNonNull(getCommand("jguiwrapper")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage("/jguiwrapper test (simple/advanced)");
            sender.sendMessage("/jguiwrapper list");
            return true;
        } else {
            String sub = args[0].toLowerCase();

            switch (sub) {
                case "test": {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("It is impossible to test from the console!");
                        return false;
                    }

                    Player player = ((Player) sender);

                    if (args.length >= 2) {
                        String gui = args[1];

                        switch (gui) {
                            case "simple":
                                new TestSimpleGui().open(player);
                            case "advanced":
                                new TestAdvancedGui().open(player);
                            default:
                                sender.sendMessage("Unknown gui");
                        }
                    }
                }

                case "list": {
                    Set<Gui> activeInstances = Gui.getActiveInstances();

                    if (activeInstances.isEmpty()) {
                        send(sender, "&cThere are no active gui instances");
                        return true;
                    }

                    int index = 0;

                    for (Gui gui : activeInstances) {
                        AbstractGui abstractGui = (AbstractGui) gui;
                        send(sender, "Index: &6" + index);
                        send(sender, "Class: &6" + abstractGui.getClass().getName());
                        send(sender, "Gui type: &6" + abstractGui.getClass().getSuperclass().getSimpleName());
                        send(sender, "Title: " + AbstractGui.LEGACY_AMPERSAND.serialize(abstractGui.title()));
                        send(sender, "Size: &6" + abstractGui.size());
                        send(sender, "Type: &6" + abstractGui.type());
                        if (abstractGui instanceof AdvancedGui) {
                            AdvancedGui advancedGui = (AdvancedGui) abstractGui;
                            Collection<GuiItemController> controllers = advancedGui.getControllers();
                            if (!controllers.isEmpty()) {
                                send(sender, "- Controllers:");
                                int i = 0;
                                for (GuiItemController controller : controllers) {
                                    i++;
                                    send(sender, "-- #&a" + i);
                                    send(sender, "--- Slots: &6" + controller.slots());
                                    send(sender, "--- Is empty: &6" + controller.isEmpty());
                                }
                            }

                            Collection<GuiDataLoader> loaders = advancedGui.getLoaders();
                            if (!loaders.isEmpty()) {
                                send(sender, "- Loaders:");
                                int i = 0;
                                for (GuiDataLoader loader : loaders) {
                                    i++;
                                    send(sender, "-- #&a" + i);
                                    send(sender, "--- Class: &6" + loader.getClass().getSimpleName());
                                }
                            }
                        }
                        send(sender, "-----------");

                        index++;
                    }
                }
            }
        }


        return true;
    }

    private void send(CommandSender sender, String text) {
        sender.sendMessage(AbstractGui.LEGACY_AMPERSAND.deserialize(text));
    }
}
