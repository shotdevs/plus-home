package com.plushome.mc.homeplugin.teleport;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import com.plushome.mc.homeplugin.model.Home;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class TeleportManager {

    private final PlusHomePlugin plugin;
    private final HashMap<UUID, BukkitTask> teleportingPlayers = new HashMap<>();

    public TeleportManager(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    public void startTeleport(Player player, Home home) {
        if (isTeleporting(player)) {
            player.sendMessage(ChatColor.RED + "You are already teleporting!");
            return;
        }

        int delay = plugin.getConfigManager().getTeleportDelay();
        if (delay <= 0) {
            player.teleport(home.getLocation());
            player.sendMessage(ChatColor.GREEN + "Teleported to " + home.getHomeName() + ".");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Teleporting to " + home.getHomeName() + " in " + delay + " seconds... Don't move!");

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(home.getLocation());
                player.sendMessage(ChatColor.GREEN + "Teleported to " + home.getHomeName() + ".");
                teleportingPlayers.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, delay * 20L);

        teleportingPlayers.put(player.getUniqueId(), task);
    }

    public void cancelTeleport(Player player) {
        if (isTeleporting(player)) {
            teleportingPlayers.get(player.getUniqueId()).cancel();
            teleportingPlayers.remove(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "Teleportation cancelled due to movement.");
        }
    }

    public boolean isTeleporting(Player player) {
        return teleportingPlayers.containsKey(player.getUniqueId());
    }
}
