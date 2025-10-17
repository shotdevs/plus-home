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
        String lang = plugin.getPlayerPreferencesManager().getPlayerLanguage(player.getUniqueId());
        if (!player.hasPermission("shothomes.use")) {
            player.sendMessage(plugin.getLanguageManager().getMessage(lang, "no-permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(plugin.getLanguageManager().getMessage(lang, "usage-sethome"));
            return true;
        }

        String homeName = args[0];
        if (homeName.length() > 32) {
            player.sendMessage(ChatColor.RED + "Home name cannot be longer than 32 characters.");
            return true;
        }

        if (homeName.trim().isEmpty() || !homeName.matches("^[a-zA-Z0-9_]+$")) {
            player.sendMessage(plugin.getLanguageManager().getMessage(lang, "invalid-name"));
            return true;
        }

        int maxHomes = getMaxHomes(player);
        int currentHomes = plugin.getDatabaseManager().getHomes(player.getUniqueId()).size();

        if (currentHomes >= maxHomes) {
            player.sendMessage(plugin.getLanguageManager().getMessage(lang, "max-homes").replace("%max%", String.valueOf(maxHomes)));
            return true;
        }

        Home home = new Home(player.getUniqueId(), homeName, player.getLocation());
        plugin.getDatabaseManager().saveHome(home);

        player.sendMessage(plugin.getLanguageManager().getMessage(lang, "set-home-success").replace("%home%", homeName));
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
