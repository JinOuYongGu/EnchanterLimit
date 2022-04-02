package me.jinou.EnchantLimit;

import io.github.thebusybiscuit.slimefun4.api.events.AutoEnchantEvent;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
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
        Objects.requireNonNull(Bukkit.getPluginCommand("EnchantLimit")).setExecutor(pluginCmd);
    }

    @Override
    public void onDisable() {
    }

    /**
     * 判断是否限制粘液科技自动附魔机
     *
     * @param event 粘液科技自动附魔事件
     */
    @EventHandler
    public void onAutoEnchanterWork(AutoEnchantEvent event) {
        if (!PluginConfig.isEnableNumLimit()) {
            return;
        }

        ItemStack enchantItem = event.getItem();
        if (canBeEnchantByNum(enchantItem)) {
            return;
        }

        event.setCancelled(true);
    }

    /**
     * 限制原版铁砧附魔机制
     *
     * @param event 原版铁砧附魔事件
     */
    @EventHandler
    public void onAnvilEnchant(InventoryClickEvent event) {
        if (!PluginConfig.isEnableVanillaNumLimit()) {
            return;
        }

        Inventory inv = event.getInventory();
        InventoryType invType = inv.getType();
        int clickSlot = event.getSlot();
        if (!(invType == InventoryType.ANVIL && clickSlot == 2)) {
            return;
        }

        ItemStack enchantItem = event.getInventory().getItem(event.getSlot());
        if (enchantItem == null || canBeEnchantByNum(enchantItem)) {
            return;
        }

        event.setCancelled(true);
        String noticeMsg = PluginConfig.getVanNumLimitReached().replace("%max%", String.valueOf(PluginConfig.getMaxNum()));
        event.getWhoClicked().sendMessage(noticeMsg);
    }

    private boolean canBeEnchantByNum(ItemStack itemStack) {
        Map<Enchantment, Integer> enchantMap = itemStack.getEnchantments();
        return enchantMap.size() <= PluginConfig.getMaxNum();
    }

    /**
     * 限制原版附魔台附魔机制
     *
     * @param event 原版铁砧附魔事件
     */
    @EventHandler
    public void onEnchantInTable(EnchantItemEvent event) {
        if (!PluginConfig.isEnableVanillaNumLimit()) {
            return;
        }

        Map<Enchantment, Integer> enchantsToAdd = event.getEnchantsToAdd();
        ItemStack itemToBeEnchanted = event.getItem();
        ItemStack expectResult = getEnchantTableResult(itemToBeEnchanted, enchantsToAdd);
        if (canBeEnchantByNum(expectResult)) {
            return;
        }

        event.setCancelled(true);
        String noticeMsg = PluginConfig.getVanNumLimitReached().replace("%max%", String.valueOf(PluginConfig.getMaxNum()));
        event.getEnchanter().sendMessage(noticeMsg);
    }

    private ItemStack getEnchantTableResult(ItemStack itemToBeEnchanted, Map<Enchantment, Integer> enchantsToAddMap) {
        Map<Enchantment, Integer> enchantMap = new HashMap<>(itemToBeEnchanted.getEnchantments());
        for (Enchantment enchantToAdd : enchantsToAddMap.keySet()) {
            if (!enchantMap.containsKey(enchantToAdd)) {
                enchantMap.put(enchantToAdd, enchantsToAddMap.get(enchantToAdd));
            } else {
                Integer addLevel = enchantsToAddMap.get(enchantToAdd);
                Integer curLevel = enchantMap.get(enchantToAdd);
                Integer largerEnchantLevel = addLevel > curLevel ? addLevel : curLevel;
                enchantMap.put(enchantToAdd, largerEnchantLevel);
            }
        }

        ItemStack expectItem = itemToBeEnchanted.clone();
        expectItem.addEnchantments(enchantMap);
        return expectItem;
    }
}
