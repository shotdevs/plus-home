package com.plushome.mc.homeplugin.gui;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import com.plushome.mc.homeplugin.gui.holder.DeleteConfirmationGUIHolder;
import com.plushome.mc.homeplugin.gui.holder.HomeGUIHolder;
import com.plushome.mc.homeplugin.gui.holder.HomeSettingsGUIHolder;
import com.plushome.mc.homeplugin.gui.holder.LanguageGUIHolder;
import com.plushome.mc.homeplugin.model.Home;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIManager {

    private final PlusHomePlugin plugin;

    public GUIManager(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    public void openHomeGUI(Player player) {
        List<Home> homes = plugin.getDatabaseManager().getHomes(player.getUniqueId());
        String lang = plugin.getPlayerPreferencesManager().getPlayerLanguage(player.getUniqueId());
        String title = plugin.getLanguageManager().getMessage(lang, "home-gui-title").replace("%player%", player.getName());
        HomeGUIHolder holder = new HomeGUIHolder();
        Inventory gui = Bukkit.createInventory(holder, 27, title);
        holder.setInventory(gui);

        for (int i = 0; i < homes.size(); i++) {
            if (i >= 26) {
                break;
            }
            Home home = homes.get(i);
            ItemStack item = new ItemStack(Material.BLUE_BED);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + home.getHomeName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Left-click to teleport");
            lore.add(ChatColor.GRAY + "Right-click for options");
            meta.setLore(lore);
            item.setItemMeta(meta);
            gui.setItem(i, item);
        }

        // Add a settings item
        ItemStack settingsItem = new ItemStack(Material.COMPARATOR);
        ItemMeta settingsMeta = settingsItem.getItemMeta();
        settingsMeta.setDisplayName(ChatColor.GREEN + "Settings");
        settingsItem.setItemMeta(settingsMeta);
        gui.setItem(26, settingsItem);

        player.openInventory(gui);
    }

    public void openHomeSettingsGUI(Player player, String homeName) {
        String lang = plugin.getPlayerPreferencesManager().getPlayerLanguage(player.getUniqueId());
        String title = plugin.getLanguageManager().getMessage(lang, "home-settings-title").replace("%home%", homeName);
        HomeSettingsGUIHolder holder = new HomeSettingsGUIHolder(homeName);
        Inventory gui = Bukkit.createInventory(holder, 27, title);
        holder.setInventory(gui);

        // Rename option
        ItemStack renameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = renameItem.getItemMeta();
        renameMeta.setDisplayName(plugin.getLanguageManager().getMessage(lang, "rename-home"));
        renameItem.setItemMeta(renameMeta);
        gui.setItem(11, renameItem);

        // Delete option
        ItemStack deleteItem = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = deleteItem.getItemMeta();
        deleteMeta.setDisplayName(plugin.getLanguageManager().getMessage(lang, "delete-home"));
        deleteItem.setItemMeta(deleteMeta);
        gui.setItem(15, deleteItem);

        // Back button
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(plugin.getLanguageManager().getMessage(lang, "back"));
        backButton.setItemMeta(backMeta);
        gui.setItem(26, backButton);

        player.openInventory(gui);
    }

    public void openLanguageGUI(Player player) {
        String playerLang = plugin.getPlayerPreferencesManager().getPlayerLanguage(player.getUniqueId());
        String title = plugin.getLanguageManager().getMessage(playerLang, "language-settings-title");
        LanguageGUIHolder holder = new LanguageGUIHolder();
        Inventory gui = Bukkit.createInventory(holder, 27, title);
        holder.setInventory(gui);

        int slot = 0;
        for (String lang : plugin.getLanguageManager().getLanguages().keySet()) {
            ItemStack langItem = new ItemStack(Material.BOOK);
            ItemMeta langMeta = langItem.getItemMeta();
            langMeta.setDisplayName(ChatColor.GREEN + lang);
            langItem.setItemMeta(langMeta);
            gui.setItem(slot++, langItem);
            if (slot >= 26) break;
        }

        // Back button
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(plugin.getLanguageManager().getMessage("en", "back"));
        backButton.setItemMeta(backMeta);
        gui.setItem(26, backButton);

        player.openInventory(gui);
    }

    public void openDeleteConfirmationGUI(Player player, String homeName) {
        String lang = plugin.getPlayerPreferencesManager().getPlayerLanguage(player.getUniqueId());
        String title = plugin.getLanguageManager().getMessage(lang, "confirm-deletion");
        DeleteConfirmationGUIHolder holder = new DeleteConfirmationGUIHolder(homeName);
        Inventory gui = Bukkit.createInventory(holder, 27, title);
        holder.setInventory(gui);

        // Confirm button
        ItemStack confirmItem = new ItemStack(Material.GREEN_WOOL);
        ItemMeta confirmMeta = confirmItem.getItemMeta();
        confirmMeta.setDisplayName(plugin.getLanguageManager().getMessage(lang, "confirm"));
        List<String> confirmLore = new ArrayList<>();
        confirmLore.add(plugin.getLanguageManager().getMessage(lang, "delete-home-lore").replace("%home%", homeName));
        confirmMeta.setLore(confirmLore);
        confirmItem.setItemMeta(confirmMeta);
        gui.setItem(11, confirmItem);

        // Cancel button
        ItemStack cancelItem = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelMeta = cancelItem.getItemMeta();
        cancelMeta.setDisplayName(plugin.getLanguageManager().getMessage(lang, "cancel"));
        cancelItem.setItemMeta(cancelMeta);
        gui.setItem(15, cancelItem);

        player.openInventory(gui);
    }
}
