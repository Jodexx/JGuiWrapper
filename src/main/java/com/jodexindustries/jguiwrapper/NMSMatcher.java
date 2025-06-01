package com.jodexindustries.jguiwrapper;

import com.jodexindustries.jguiwrapper.nms.NMSWrapper;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class NMSMatcher {

    private static final Map<String, String> VERSIONS = Map.of("1.20.5", "1_20_R4", "1.20.6", "1_20_R4", "1.21.1", "1_21_R1", "1.21.3", "1_21_R2", "1.21.4", "1_21_R3", "1.21.5", "1_21_R4");
    private static final String NEWEST_VERSION = "1_21_R4";

    private static boolean initialized;
    private static NMSWrapper wrapper;

    public static NMSWrapper getWrapper() {
        if (!initialized) {
            initialized = true;
            return wrapper = initWrapper();
        } else if (wrapper == null) {
            throw new RuntimeException("The previous attempt to initialize the version wrapper failed. " +
                    "This could be because this server version is not supported or " +
                    "because an error occured during initialization.");
        } else {
            return wrapper;
        }
    }

    private static NMSWrapper initWrapper() {
        String craftBukkitPackage = Bukkit.getServer().getClass().getPackage().getName();
        String version = craftBukkitPackage.contains(".v") ? craftBukkitPackage.split("\\.")[3].substring(1) :
                VERSIONS.getOrDefault(Bukkit.getBukkitVersion().split("-")[0], NEWEST_VERSION);

        final String className = NMSWrapper.class.getPackage().getName() + ".Wrapper" + version;

        try {
            return (NMSWrapper) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException exception) {
            throw new RuntimeException("Failed to load support for server version " + version, exception);
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("JGuiWrapper does not support the server version \"" + version + "\"", exception);
        }
    }

}