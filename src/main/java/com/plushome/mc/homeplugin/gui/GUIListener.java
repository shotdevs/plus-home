package com.plushome.mc.homeplugin.gui;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import com.plushome.mc.homeplugin.gui.holder.DeleteConfirmationGUIHolder;
import com.plushome.mc.homeplugin.gui.holder.HomeGUIHolder;
import com.plushome.mc.homeplugin.gui.holder.HomeSettingsGUIHolder;
import com.plushome.mc.homeplugin.gui.holder.LanguageGUIHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    private final PlusHomePlugin plugin;

    public GUIListener(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof HomeGUIHolder || holder instanceof HomeSettingsGUIHolder || holder instanceof LanguageGUIHolder || holder instanceof DeleteConfirmationGUIHolder) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            if (holder instanceof HomeGUIHolder) {
                handleHomeGUIClick(event, player);
            } else if (holder instanceof HomeSettingsGUIHolder) {
                handleHomeSettingsGUIClick(event, player, (HomeSettingsGUIHolder) holder);
            } else if (holder instanceof LanguageGUIHolder) {
                handleLanguageGUIClick(event, player);
            } else if (holder instanceof DeleteConfirmationGUIHolder) {
                handleDeleteConfirmationGUIClick(event, player, (DeleteConfirmationGUIHolder) holder);
            }
        }
    }

    private void handleHomeGUIClick(InventoryClickEvent event, Player player) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem.getType() == Material.BLUE_BED) {
            String homeName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (event.isRightClick()) {
                plugin.getGuiManager().openHomeSettingsGUI(player, homeName);
            } else {
                plugin.getDatabaseManager().getHome(player.getUniqueId(), homeName).ifPresent(home -> {
                    plugin.getTeleportManager().startTeleport(player, home);
                    player.closeInventory();
                });
            }
        } else if (clickedItem.getType() == Material.COMPARATOR) {
            plugin.getGuiManager().openLanguageGUI(player);
        }
    }

    private void handleHomeSettingsGUIClick(InventoryClickEvent event, Player player, HomeSettingsGUIHolder holder) {
        ItemStack clickedItem = event.getCurrentItem();
        String homeName = holder.getHomeName();

        if (clickedItem.getType().equals(Material.NAME_TAG)) {
            plugin.getRenameListener().startRenaming(player, homeName);
        } else if (clickedItem.getType().equals(Material.BARRIER)) {
            plugin.getGuiManager().openDeleteConfirmationGUI(player, homeName);
        } else if (clickedItem.getType().equals(Material.ARROW)) {
            plugin.getGuiManager().openHomeGUI(player);
        }
    }

    private void handleLanguageGUIClick(InventoryClickEvent event, Player player) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem.getType() == Material.BOOK) {
            String lang = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            plugin.getPlayerPreferencesManager().setPlayerLanguage(player.getUniqueId(), lang);
            player.sendMessage(ChatColor.GREEN + "Language set to " + lang);
            player.closeInventory();
        } else if (clickedItem.getType() == Material.ARROW) {
            plugin.getGuiManager().openHomeGUI(player);
        }
    }

    private void handleDeleteConfirmationGUIClick(InventoryClickEvent event, Player player, DeleteConfirmationGUIHolder holder) {
        ItemStack clickedItem = event.getCurrentItem();
        String homeName = holder.getHomeName();
        String lang = plugin.getPlayerPreferencesManager().getPlayerLanguage(player.getUniqueId());

        if (clickedItem.getType() == Material.GREEN_WOOL) {
            plugin.getDatabaseManager().deleteHome(player.getUniqueId(), homeName);
            player.sendMessage(plugin.getLanguageManager().getMessage(lang, "delete-home-success").replace("%home%", homeName));
            player.closeInventory();
            plugin.getGuiManager().openHomeGUI(player);
        } else if (clickedItem.getType() == Material.RED_WOOL) {
            plugin.getGuiManager().openHomeSettingsGUI(player, homeName);
        }
    }
}
