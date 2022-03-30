package me.jinou.EnchantLimit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PluginCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        String reloadCmd = "reload";
        if (sender.isOp() && args[0].equalsIgnoreCase(reloadCmd)) {
            PluginConfig.reloadConfig();
            sender.sendMessage("EnchantLimit 重载完成");
        }
        return false;
    }
}
