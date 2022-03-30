package me.jinou.EnchantLimit;

import io.github.thebusybiscuit.slimefun4.api.events.AutoEnchantEvent;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;

/**
 * @author Jin_ou
 */
public final class EnchantLimit extends JavaPlugin implements Listener {
    static private EnchantLimit plugin = null;

    static EnchantLimit getInstance() {
        return EnchantLimit.plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        PluginConfig.loadConfig(getConfig());

        Bukkit.getPluginManager().registerEvents(this, this);
        PluginCmd pluginCmd = new PluginCmd();
        Objects.requireNonNull(Bukkit.getPluginCommand("sfenchanterlimit")).setExecutor(pluginCmd);
    }

    @Override
    public void onDisable() {
    }

    /**
     * 判断是否限制粘液科技自动附魔机
     * @param event 粘液科技自动附魔事件
     */
    @EventHandler
    public void onAutoEnchanterWork(AutoEnchantEvent event) {
        if (!PluginConfig.isEnableNumLimit()) {
            return;
        }

        ItemStack enchantItem = event.getItem();
        if (!canBeEnchant(enchantItem)) {
            event.setCancelled(true);
        }
    }

    /**
     * 判断是否限制原版附魔机制
     * @param event 原版铁砧附魔事件
     */
    @EventHandler
    public void onVanillaEnchant(PrepareItemEnchantEvent event) {
        if (!PluginConfig.isEnableVanillaNumLimit()) {
            return;
        }

        ItemStack enchantItem = event.getItem();
        if (!canBeEnchant(enchantItem)) {
            event.setCancelled(true);
            String noticeMsg = PluginConfig.getVanNumLimitReached().replace("%max%", String.valueOf(PluginConfig.getMaxNum()));
            event.getEnchanter().sendMessage(noticeMsg);
        }
    }

    private boolean canBeEnchant(ItemStack itemStack) {
        Map<Enchantment, Integer> enchantMap = itemStack.getEnchantments();
        return enchantMap.size() < PluginConfig.getMaxNum();
    }
}
