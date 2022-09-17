package xyz.n7mn.dev.survivalsystem.util;

import org.bukkit.configuration.file.YamlConfiguration;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.io.File;
import java.util.List;

public class MessageManager {
    private static YamlConfiguration messageConfig;
    private static String prefix;

    public static Object get(String key) {
        return messageConfig.get(key);
    }

    public static boolean getBoolean(String key) {
        return messageConfig.getBoolean(key);
    }

    public static int getInt(String key) {
        return messageConfig.getInt(key);
    }

    public static String getString(String key) {
        return MessageUtil.translateAlternateColorCodes(messageConfig.getString(key).replaceAll("%prefix%", prefix));
    }

    public static List<String> getStringList(String key) {
        return messageConfig.getStringList(key);
    }

    public static YamlConfiguration getYamlConfiguration() {
        return messageConfig;
    }

    public static void reload() {
        messageConfig = YamlConfiguration.loadConfiguration(new File(SurvivalInstance.INSTANCE.getPlugin().getDataFolder(), "message.yml"));

        prefix = getString("PREFIX");
    }

    public static void init() {
        if (!new File(SurvivalInstance.INSTANCE.getPlugin().getDataFolder(), "message.yml").exists())
            SurvivalInstance.INSTANCE.getPlugin().saveResource("message.yml", false);
        reload();
    }
}

