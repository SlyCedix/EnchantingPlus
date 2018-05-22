package com.github.slycedix.eplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEplus implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("eplus")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                boolean success = false;
                if(args.length >= 1 && args[0].equalsIgnoreCase("admin")) {
                    if(sender.hasPermission("eplus.admin")) {
                        success = EnchantingGui.openGUI(p, true);
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to open that menu");
                    }
                } else {
                    success = EnchantingGui.openGUI(p, false);
                }
                if (!success) {
                    sender.sendMessage(ChatColor.DARK_RED + "Could not open enchanting gui");
                }
                return true;
            } else {
                sender.sendMessage("You are not a player");
            }
        } else {
            sender.sendMessage("That is not a valid argument");
            return true;
        }
        return true;
    }
}