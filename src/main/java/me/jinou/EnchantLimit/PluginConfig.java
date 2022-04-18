package me.jinou.EnchantLimit;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

public class PluginConfig {
    private static final EnchantLimit PLUGIN = EnchantLimit.getInstance();
    private static FileConfiguration fileConfig = null;

    @Getter
    private static boolean enableSlimefunNumLimit = false;
    @Getter
    private static boolean enableVanillaNumLimit = false;
    @Getter
    private static int maxNumVanilla = 5;
    @Getter
    private static int maxNumSlimefun = 5;

    @Getter
    private static String vanNumLimitReached = "";
    @Getter
    private static String sfNumLimitReached = "";

    static void loadConfig(FileConfiguration fileConfig) {
        PluginConfig.fileConfig = fileConfig;
        enableSlimefunNumLimit = fileConfig.getBoolean("limit-num.slimefun-enable");
        enableVanillaNumLimit = fileConfig.getBoolean("limit-num.limit-vanilla");
        maxNumVanilla = fileConfig.getInt("limit-num.vanilla-max-num");
        maxNumSlimefun = fileConfig.getInt("limit-num.slimefun-enchanter-max-num");
        vanNumLimitReached = fileConfig.getString("message.vanilla-num-limit-reached");
        sfNumLimitReached = fileConfig.getString("message.slimefun-num-limit-reached");
    }

    public static void reloadConfig() {
        PLUGIN.reloadConfig();
        loadConfig(PLUGIN.getConfig());
    }

	public static String getVanNumLimitReached() {
		// TODO Auto-generated method stub
		return vanNumLimitReached;
	}

	public static boolean isEnableSlimefunNumLimit() {
		// TODO Auto-generated method stub
		return enableSlimefunNumLimit;
	}

	public static int getMaxNumSlimefun() {
		// TODO Auto-generated method stub
		return maxNumSlimefun;
	}

	public static String getSfNumLimitReached() {
		// TODO Auto-generated method stub
		return sfNumLimitReached;
	}

	public static boolean isEnableVanillaNumLimit() {
		// TODO Auto-generated method stub
		return enableVanillaNumLimit;
	}

	public static Integer getMaxNumVanilla() {
		// TODO Auto-generated method stub
		return maxNumVanilla;
	}
}
