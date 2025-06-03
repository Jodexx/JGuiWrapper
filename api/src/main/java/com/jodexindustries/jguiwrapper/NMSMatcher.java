package com.jodexindustries.jguiwrapper;

import com.jodexindustries.jguiwrapper.exception.JGuiWrapperVersionException;
import com.jodexindustries.jguiwrapper.nms.NMSWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class NMSMatcher {

    private static final Map<String, String> VERSIONS = Map.of(
            "1.20.5", "1_20_R4",
            "1.20.6", "1_20_R4",
            "1.21.1", "1_21_R1",
            "1.21.3", "1_21_R2",
            "1.21.4", "1_21_R3",
            "1.21.5", "1_21_R4"
    );

    private static final String NEWEST_VERSION = "1_21_R4";

    private static boolean warned = false;
    private static boolean initialized = false;
    private static NMSWrapper wrapper;

    public static final NMSWrapper EMPTY_WRAPPER = (player, title) -> {
        if (!warned) {
            JGuiInitializer.getPlugin().getLogger().warning(
                    "NMSWrapper not loaded! Inventory view can't be updated."
            );
            warned = true;
        }
    };

    public static NMSWrapper getWrapper(Plugin plugin) throws JGuiWrapperVersionException {
        if (!initialized) {
            initialized = true;
            return wrapper = initWrapper(plugin);
        }

        if (wrapper == null) {
            throw new JGuiWrapperVersionException(
                    "The previous attempt to initialize the version wrapper failed. " +
                            "This could be because this server version is not supported or " +
                            "because an error occurred during initialization."
            );
        }

        return wrapper;
    }

    private static NMSWrapper initWrapper(Plugin plugin) throws JGuiWrapperVersionException {
        String craftBukkitPackage = Bukkit.getServer().getClass().getPackage().getName();

        String version = craftBukkitPackage.contains(".v")
                ? craftBukkitPackage.split("\\.")[3].substring(1)
                : VERSIONS.getOrDefault(Bukkit.getBukkitVersion().split("-")[0], NEWEST_VERSION);

        String className = NMSWrapper.class.getPackage().getName() + ".Wrapper" + version;

        plugin.getLogger().info("Using " + version + " NMS");

        try {
            return (NMSWrapper) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new JGuiWrapperVersionException(
                    "JGuiWrapper does not support the server version \"" + version + "\"", e
            );
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 NoClassDefFoundError | InvocationTargetException e) {
            throw new JGuiWrapperVersionException(
                    "Failed to load support for server version " + version, e
            );
        }
    }
}
