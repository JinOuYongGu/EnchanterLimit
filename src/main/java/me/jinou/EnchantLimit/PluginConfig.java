package me.jinou.EnchantLimit;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class PluginConfig {
    private static final EnchantLimit PLUGIN = EnchantLimit.getInstance();
    private static FileConfiguration fileConfig = null;

    @Getter
    private static boolean enableNumLimit = false;
    @Getter
    private static boolean enableVanillaNumLimit = false;
    @Getter
    private static int maxNum = 5;

    @Getter
    private static String vanNumLimitReached = "";

    static void loadConfig(FileConfiguration fileConfig) {
        PluginConfig.fileConfig = fileConfig;
        enableNumLimit = fileConfig.getBoolean("limit-num.enable");
        enableVanillaNumLimit = fileConfig.getBoolean("limit-num.limit-vanilla");
        maxNum = fileConfig.getInt("limit-num.max-num");
        vanNumLimitReached = fileConfig.getString("message.vanilla-num-limit-reached");
    }

    public static void reloadConfig() {
        PLUGIN.reloadConfig();
        loadConfig(PLUGIN.getConfig());
    }
}
