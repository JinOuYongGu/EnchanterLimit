package me.jinou.sfenchanterlimit;

import io.github.thebusybiscuit.slimefun4.api.events.AutoEnchantEvent;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;

public final class SfEnchanterLimit extends JavaPlugin implements Listener {
    static private SfEnchanterLimit plugin = null;

    static SfEnchanterLimit getInstance() {
        return SfEnchanterLimit.plugin;
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
        // Plugin shutdown logic
    }

    @EventHandler
    public void onAutoEnchanterWork(AutoEnchantEvent event) {
        ItemStack itemStack = event.getItem();
        if (!canBeEnchant(itemStack)) {
            event.setCancelled(true);
        }
    }

    private boolean canBeEnchant(ItemStack itemStack) {
        Map<Enchantment, Integer> enchantMap = itemStack.getEnchantments();
        return enchantMap.size() < PluginConfig.getMaxNum();
    }
}
