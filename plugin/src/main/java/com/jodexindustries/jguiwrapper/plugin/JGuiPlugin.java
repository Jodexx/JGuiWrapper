package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.common.JGuiInitializer;
import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import com.jodexindustries.jguiwrapper.plugin.gui.TestAbstractGui;
import com.jodexindustries.jguiwrapper.plugin.gui.TestAdvancedGui;
import com.jodexindustries.jguiwrapper.plugin.gui.TestSimpleGui;
import com.jodexindustries.jguiwrapper.plugin.gui.item.TestGuiLoader;
import com.jodexindustries.jguiwrapper.plugin.gui.item.TestItemHandler;
import net.kyori.adventure.key.Key;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings({"unused"})
public final class JGuiPlugin extends JavaPlugin {

    public static final Key TEST_LOADER_KEY = Key.key("jguiwrapper", "test");
    public static final Key TEST_HANDLER_KEY = Key.key("jguiwrapper", "test");

    @Override
    public void onEnable() {
        JGuiInitializer.init(this);

        GlobalRegistry registry = JGuiInitializer.get().getRegistry();
        registry.registerLoader(TEST_LOADER_KEY, new TestGuiLoader());
        registry.registerHandler(TEST_HANDLER_KEY, new TestItemHandler());

        Objects.requireNonNull(getCommand("jguiwrapper")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage("/jguiwrapper test (abstract/simple/advanced)");
            sender.sendMessage("/jguiwrapper list");
            return true;
        } else {
            String sub = args[0].toLowerCase();

            switch (sub) {
                case "test": {
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage("It is impossible to test from the console!");
                        return false;
                    }

                    if (args.length >= 2) {
                        String gui = args[1];

                        switch (gui) {
                            case "abstract":
                                new TestAbstractGui().open(player);
                                break;
                            case "simple":
                                new TestSimpleGui().open(player);
                                break;
                            case "advanced":
                                new TestAdvancedGui().open(player);
                                break;
                            default:
                                sender.sendMessage("Unknown gui");
                                break;
                        }
                    }
                    break;
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
                        send(sender, "Title: " + JGuiInitializer.get().defaultSerializer().serialize(abstractGui.title()));
                        send(sender, "Size: &6" + abstractGui.size());
                        send(sender, "Type: &6" + abstractGui.type());
                        if (abstractGui instanceof AdvancedGui advancedGui) {
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
                    break;
                }
            }
        }


        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("test", "list");
        }

        if (args.length == 2 && args[0].equals("test")) {
            return Arrays.asList("abstract", "simple", "advanced");
        }

        return Collections.emptyList();
    }

    private void send(CommandSender sender, String text) {
        sender.sendMessage(JGuiInitializer.get().defaultSerializer().deserialize(text));
    }
}
