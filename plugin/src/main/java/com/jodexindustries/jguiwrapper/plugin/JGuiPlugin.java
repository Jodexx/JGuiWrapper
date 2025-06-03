package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.JGuiInitializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class JGuiPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        JGuiInitializer.init(this);
        Objects.requireNonNull(getCommand("jguiwrapper")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) return false;

        TestGui gui = new TestGui();

        gui.open(player);

        return true;
    }
}
