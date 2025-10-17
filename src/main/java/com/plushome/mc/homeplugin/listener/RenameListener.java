package com.plushome.mc.homeplugin.listener;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RenameListener implements Listener {

    private final PlusHomePlugin plugin;
    private final Map<UUID, String> renaming = new HashMap<>();

    public RenameListener(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    public void startRenaming(Player player, String oldHomeName) {
        renaming.put(player.getUniqueId(), oldHomeName);
        player.closeInventory();
        String lang = plugin.getPlayerPreferencesManager().getPlayerLanguage(player.getUniqueId());
        player.sendMessage(plugin.getLanguageManager().getMessage(lang, "rename-prompt"));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (renaming.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            String oldHomeName = renaming.get(player.getUniqueId());
            String newHomeName = event.getMessage();
            String lang = plugin.getPlayerPreferencesManager().getPlayerLanguage(player.getUniqueId());

            if (newHomeName.equalsIgnoreCase("cancel")) {
                renaming.remove(player.getUniqueId());
                player.sendMessage(plugin.getLanguageManager().getMessage(lang, "rename-cancel"));
                plugin.getGuiManager().openHomeGUI(player);
                return;
            }

            if (newHomeName.trim().isEmpty() || !newHomeName.matches("^[a-zA-Z0-9_]+$")) {
                player.sendMessage(plugin.getLanguageManager().getMessage(lang, "invalid-name"));
                return;
            }

            plugin.getDatabaseManager().renameHome(player.getUniqueId(), oldHomeName, newHomeName);
            renaming.remove(player.getUniqueId());
            player.sendMessage(plugin.getLanguageManager().getMessage(lang, "rename-home-success").replace("%old_home%", oldHomeName).replace("%new_home%", newHomeName));
            plugin.getGuiManager().openHomeGUI(player);
        }
    }
}