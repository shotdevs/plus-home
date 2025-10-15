package com.plushome.mc.homeplugin.config;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final PlusHomePlugin plugin;
    private FileConfiguration config;

    public ConfigManager(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public int getTeleportDelay() {
        return config.getInt("teleport-delay", 5);
    }

    public int getDefaultMaxHomes() {
        return config.getInt("default-max-homes", 5);
    }
}
