package me.jinou.sfenchanterlimit;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class PluginConfig {
    private static final SfEnchanterLimit plugin = SfEnchanterLimit.getInstance();
    private static final List<String> unlimitEnchant = null;
    private static final List<String> limitEnchant = null;
    private static final boolean enableLevelLimit = false;
    private static final boolean levelWhiteListMode = false;
    private static FileConfiguration fileConfig = null;
    private static boolean enableNumLimit = false;
    private static int maxNum = 0;
    private static boolean numWhiteListMode = false;

    static void loadConfig(FileConfiguration fileConfig) {
        PluginConfig.fileConfig = fileConfig;
        enableNumLimit = fileConfig.getBoolean("limit-num.enable");
        maxNum = fileConfig.getInt("limit-num.max-num");
        numWhiteListMode = fileConfig.getBoolean("limit-num.white-list-mode");
    }

    static int getMaxNum() {
        return maxNum;
    }

    public static void reloadConfig() {
        plugin.reloadConfig();
        loadConfig(plugin.getConfig());
    }
}
