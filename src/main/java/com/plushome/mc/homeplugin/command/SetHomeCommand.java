package com.plushome.mc.homeplugin.command;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import com.plushome.mc.homeplugin.model.Home;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {

    private final PlusHomePlugin plugin;

    public SetHomeCommand(PlusHomePlugin plugin) {
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
            player.sendMessage(ChatColor.RED + "Usage: /sethome <name>");
            return true;
        }

        String homeName = args[0];
        if (homeName.length() > 32) {
            player.sendMessage(ChatColor.RED + "Home name cannot be longer than 32 characters.");
            return true;
        }

        int maxHomes = getMaxHomes(player);
        int currentHomes = plugin.getDatabaseManager().getHomes(player.getUniqueId()).size();

        if (currentHomes >= maxHomes) {
            player.sendMessage(ChatColor.RED + "You have reached your maximum number of homes (" + maxHomes + ").");
            return true;
        }

        Home home = new Home(player.getUniqueId(), homeName, player.getLocation());
        plugin.getDatabaseManager().saveHome(home);

        player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' has been set!");
        return true;
    }

    private int getMaxHomes(Player player) {
        if (player.isOp()) {
            return Integer.MAX_VALUE;
        }
        int max = plugin.getConfigManager().getDefaultMaxHomes();
        for (int i = 100; i > 0; i--) { // Check up to 100 homes
            if (player.hasPermission("shothomes.set.multiple." + i)) {
                return i;
            }
        }
        return max;
    }
}
