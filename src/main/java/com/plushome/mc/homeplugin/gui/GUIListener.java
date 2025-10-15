package com.plushome.mc.homeplugin.gui;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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

        String title = ChatColor.AQUA + "" + ChatColor.BOLD + player.getName() + "'s Homes";
        if (!event.getView().getTitle().equals(title)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.GRAY_STAINED_GLASS_PANE || clickedItem.getType() == Material.RED_BED) {
            return;
        }

        if (clickedItem.getType() == Material.OAK_DOOR) {
            player.closeInventory();
            return;
        }

        if (clickedItem.getType() == Material.BLUE_BED) {
            String homeName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            if (event.isShiftClick()) {
                plugin.getDatabaseManager().deleteHome(player.getUniqueId(), homeName);
                player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' has been deleted.");
                player.closeInventory();
                plugin.getGuiManager().openHomeGUI(player);
            } else {
                plugin.getDatabaseManager().getHome(player.getUniqueId(), homeName).ifPresent(home -> {
                    plugin.getTeleportManager().startTeleport(player, home);
                    player.closeInventory();
                });
            }
        }
    }
}
