package com.plushome.mc.homeplugin.gui;

import com.plushome.mc.homeplugin.PlusHomePlugin;
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
        String title = ChatColor.AQUA + "" + ChatColor.BOLD + player.getName() + "'s Homes";
        Inventory gui = Bukkit.createInventory(null, 54, title);

        // Fill the background with filler items
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }

        // Add a decorative bed in the center
        ItemStack centerBed = new ItemStack(Material.RED_BED);
        ItemMeta centerMeta = centerBed.getItemMeta();
        centerMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Your Homes");
        centerBed.setItemMeta(centerMeta);
        gui.setItem(22, centerBed);

        // Define the slots for homes to be placed around the center
        List<Integer> homeSlots = Arrays.asList(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21,       23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        );

        for (int i = 0; i < homes.size(); i++) {
            if (i >= homeSlots.size()) {
                break; // Stop if there are more homes than available slots
            }
            Home home = homes.get(i);
            ItemStack item = new ItemStack(Material.BLUE_BED);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + home.getHomeName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to teleport");
            lore.add(ChatColor.GRAY + "Shift-click to delete");
            meta.setLore(lore);
            item.setItemMeta(meta);
            gui.setItem(homeSlots.get(i), item);
        }

        // Add a close door item
        ItemStack closeDoor = new ItemStack(Material.OAK_DOOR);
        ItemMeta doorMeta = closeDoor.getItemMeta();
        doorMeta.setDisplayName(ChatColor.RED + "Close");
        closeDoor.setItemMeta(doorMeta);
        gui.setItem(45, closeDoor);

        player.openInventory(gui);
    }
}
