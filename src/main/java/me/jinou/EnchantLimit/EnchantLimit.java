package me.jinou.EnchantLimit;

import io.github.thebusybiscuit.slimefun4.api.events.AsyncAutoEnchanterProcessEvent;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
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
     * 限制粘液科技自动附魔机
     *
     * @param event 粘液科技自动附魔事件
     */
    @EventHandler
    public void onAutoEnchanterWork(AsyncAutoEnchanterProcessEvent event) {
        if (!PluginConfig.isEnableSlimefunNumLimit()) {
            return;
        }

        ItemStack enchantItem = event.getItem();
        ItemStack enchantBook = event.getEnchantedBook();
        EnchantmentStorageMeta enchantMeta = (EnchantmentStorageMeta) enchantBook.getItemMeta();
        Map<Enchantment, Integer> availableEnchantOnBook = new HashMap<>(16);
        for (Map.Entry<Enchantment, Integer> enchantEntry : enchantMeta.getStoredEnchants().entrySet()) {
            if (enchantEntry.getKey().canEnchantItem(enchantItem)) {
                availableEnchantOnBook.put(enchantEntry.getKey(), enchantEntry.getValue());
            }
        }
        ItemStack expectResult = getEnchantResult(enchantItem, availableEnchantOnBook);
        int resultEnchantNum = expectResult.getEnchantments().size();
        if (resultEnchantNum <= PluginConfig.getMaxNumSlimefun()) {
            return;
        }

        event.setCancelled(true);
        if (!event.getMenu().toInventory().getViewers().isEmpty()) {
            showSfEnchanterWarning(event.getMenu());
        }
    }

    private void showSfEnchanterWarning(BlockMenu menu) {
        String noticeMsg = PluginConfig.getSfNumLimitReached().replace("%max%", String.valueOf(PluginConfig.getMaxNumSlimefun()));
        ItemStack progressIcon = new CustomItemStack(Material.BARRIER, " ", noticeMsg);
        menu.replaceExistingItem(22, progressIcon);
    }

    /**
     * 限制原版铁砧附魔机制
     *
     * @param event 原版铁砧附魔事件
     */
    @EventHandler(priority = EventPriority.HIGHEST)
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
        Integer maxEnchantNum = PluginConfig.getMaxNumVanilla();
        if (enchantItem == null || enchantItem.getEnchantments().size() <= maxEnchantNum) {
            return;
        }

        event.setCancelled(true);
        String noticeMsg = PluginConfig.getVanNumLimitReached().replace("%max%", String.valueOf(maxEnchantNum));
        event.getWhoClicked().sendMessage(noticeMsg);
    }

    /**
     * 限制原版附魔台附魔机制
     *
     * @param event 原版铁砧附魔事件
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchantInTable(EnchantItemEvent event) {
        if (!PluginConfig.isEnableVanillaNumLimit()) {
            return;
        }

        Map<Enchantment, Integer> enchantsToAdd = event.getEnchantsToAdd();
        ItemStack itemToBeEnchanted = event.getItem();
        ItemStack expectResult = getEnchantResult(itemToBeEnchanted, enchantsToAdd);
        if (expectResult.getEnchantments().size() <= PluginConfig.getMaxNumVanilla()) {
            return;
        }

        event.setCancelled(true);
        String noticeMsg = PluginConfig.getVanNumLimitReached().replace("%max%", String.valueOf(PluginConfig.getMaxNumVanilla()));
        event.getEnchanter().sendMessage(noticeMsg);
    }

    private ItemStack getEnchantResult(ItemStack itemToBeEnchanted, Map<Enchantment, Integer> enchantsToAddMap) {
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
