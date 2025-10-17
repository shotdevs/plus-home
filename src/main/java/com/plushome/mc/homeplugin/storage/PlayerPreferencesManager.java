package com.plushome.mc.homeplugin.storage;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerPreferencesManager {

    private final PlusHomePlugin plugin;
    private final File preferencesFile;
    private FileConfiguration preferencesConfig;

    public PlayerPreferencesManager(PlusHomePlugin plugin) {
        this.plugin = plugin;
        this.preferencesFile = new File(plugin.getDataFolder(), "player_preferences.yml");
        if (!preferencesFile.exists()) {
            try {
                preferencesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create player_preferences.yml", e);
            }
        }
        this.preferencesConfig = YamlConfiguration.loadConfiguration(preferencesFile);
    }

    public String getPlayerLanguage(UUID playerUUID) {
        return preferencesConfig.getString(playerUUID.toString() + ".language", "en");
    }

    public void setPlayerLanguage(UUID playerUUID, String language) {
        preferencesConfig.set(playerUUID.toString() + ".language", language);
        save();
    }

    private void save() {
        try {
            preferencesConfig.save(preferencesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save player_preferences.yml", e);
        }
    }
}