package com.jodexindustries.jguiwrapper.paper.nms;

import com.jodexindustries.jguiwrapper.paper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.paper.exception.JGuiWrapperVersionException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class NMSMatcher {

    /**
     * A map that stores NMS version names for newer Minecraft versions starting from 1.20.5.
     */
    private static final Map<String, String> VERSIONS = new HashMap<>();

    static {
        VERSIONS.put("1.20.5", "1_20_R4");
        VERSIONS.put("1.20.6", "1_20_R4");
        VERSIONS.put("1.21", "1_21_R1");
        VERSIONS.put("1.21.1", "1_21_R1");
        VERSIONS.put("1.21.3", "1_21_R2");
        VERSIONS.put("1.21.4", "1_21_R3");
        VERSIONS.put("1.21.5", "1_21_R4");
        VERSIONS.put("1.21.6", "1_21_R5");
        VERSIONS.put("1.21.7", "1_21_R5");
        VERSIONS.put("1.21.8", "1_21_R5");
    }

    private static final String NEWEST_VERSION = "1_21_R5";

    private static boolean initialized = false;
    private static NMSWrapper wrapper;

    public static NMSWrapper getWrapper(Plugin plugin, boolean log) throws JGuiWrapperVersionException {
        if (!initialized) {
            initialized = true;
            return wrapper = initWrapper(plugin, log);
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

    private static NMSWrapper initWrapper(Plugin plugin, boolean log) throws JGuiWrapperVersionException {
        String craftBukkitPackage = Bukkit.getServer().getClass().getPackage().getName();

        String version = craftBukkitPackage.contains(".v")
                ? craftBukkitPackage.split("\\.")[3].substring(1)
                : VERSIONS.getOrDefault(Bukkit.getBukkitVersion().split("-")[0], NEWEST_VERSION);

        String className = NMSMatcher.class.getPackage().getName() + ".Wrapper" + version;

        if (log) plugin.getLogger().info("Using " + version + " NMS");

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
