package com.plushome.mc.homeplugin.command;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {

    private final PlusHomePlugin plugin;

    public DelHomeCommand(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("shothomes.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /delhome <name>");
            return true;
        }

        String homeName = args[0];
        plugin.getDatabaseManager().deleteHome(player.getUniqueId(), homeName);
        player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' has been deleted.");
        return true;
    }
}
