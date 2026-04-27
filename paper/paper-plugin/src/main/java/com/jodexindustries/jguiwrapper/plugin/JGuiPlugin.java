package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.common.PaperGuiApiImpl;
import com.jodexindustries.jguiwrapper.paper.api.gui.PaperGui;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.PaperGuiBase;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.advanced.PaperAdvancedGui;
import com.jodexindustries.jguiwrapper.plugin.gui.TestAbstractGui;
import com.jodexindustries.jguiwrapper.plugin.gui.TestAdvancedGui;
import com.jodexindustries.jguiwrapper.plugin.gui.TestPaginatedAdvancedGui;
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
import java.util.function.Supplier;

public final class JGuiPlugin extends JavaPlugin {

    public static final Key TEST_LOADER_KEY = Key.key("jguiwrapper", "test");
    public static final Key TEST_HANDLER_KEY = Key.key("jguiwrapper", "test");

    private static final Map<String, Supplier<PaperGui>> TEST_GUIS = Map.of(
            "abstract", TestAbstractGui::new,
            "simple", TestSimpleGui::new,
            "advanced", TestAdvancedGui::new,
            "paginated", TestPaginatedAdvancedGui::new
    );

    @Override
    public void onEnable() {
        PaperGuiApiImpl.init(this);

        GlobalRegistry registry = PaperGuiApiImpl.get().getRegistry();
        registry.registerLoader(TEST_LOADER_KEY, new TestGuiLoader());
        registry.registerHandler(TEST_HANDLER_KEY, new TestItemHandler());

        Objects.requireNonNull(getCommand("jguiwrapper")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage("/jguiwrapper test (abstract/simple/advanced/paginated)");
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
                        PaperGui gui = TEST_GUIS.get(args[1]).get();
                        if (gui == null) {
                            sender.sendMessage("Unknown gui");
                            return false;
                        }

                        gui.open(player);
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
                        PaperGuiBase<?> abstractGui = (PaperGuiBase<?>) gui;
                        send(sender, "Index: &6" + index);
                        send(sender, "Class: &6" + abstractGui.getClass().getName());
                        send(sender, "Gui type: &6" + abstractGui.getClass().getSuperclass().getSimpleName());
                        send(sender, "Title: " + PaperGuiApiImpl.get().defaultSerializer().serialize(abstractGui.title()));
                        send(sender, "Size: &6" + abstractGui.size());
                        send(sender, "Type: &6" + abstractGui.type());
                        if (abstractGui instanceof PaperAdvancedGui advancedGui) {
                            Collection<AdvancedGuiItemController<PaperAdvancedGui, ?>> controllers = advancedGui.getControllers();
                            if (!controllers.isEmpty()) {
                                send(sender, "- Controllers:");
                                int i = 0;
                                for (AdvancedGuiItemController<PaperAdvancedGui, ?> controller : controllers) {
                                    i++;
                                    send(sender, "-- #&a" + i);
                                    send(sender, "--- Slots: &6" + controller.slots());
                                    send(sender, "--- Is empty: &6" + controller.isEmpty());
                                }
                            }

                            Collection<GuiDataLoader<PaperAdvancedGui>> loaders = advancedGui.getLoaders();
                            if (!loaders.isEmpty()) {
                                send(sender, "- Loaders:");
                                int i = 0;
                                for (GuiDataLoader<PaperAdvancedGui> loader : loaders) {
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
            return Arrays.asList("abstract", "simple", "advanced", "paginated");
        }

        return Collections.emptyList();
    }

    private void send(CommandSender sender, String text) {
        sender.sendMessage(PaperGuiApiImpl.get().defaultSerializer().deserialize(text));
    }
}
