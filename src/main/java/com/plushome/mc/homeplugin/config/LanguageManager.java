package com.plushome.mc.homeplugin.config;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class LanguageManager {

    private final PlusHomePlugin plugin;
    private final Map<String, FileConfiguration> languages = new HashMap<>();
    private String defaultLang = "en";

    public LanguageManager(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    public void loadLanguages() {
        File langFolder = new File(plugin.getDataFolder(), "languages");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        // Save default language file
        saveDefaultLanguageFile();

        File[] langFiles = langFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (langFiles != null) {
            for (File langFile : langFiles) {
                String langName = langFile.getName().replace(".yml", "");
                FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
                languages.put(langName, langConfig);
            }
        }
    }

    private void saveDefaultLanguageFile() {
        String langFileName = "messages_" + defaultLang + ".yml";
        File langFile = new File(plugin.getDataFolder(), "languages/" + langFileName);
        if (!langFile.exists()) {
            plugin.saveResource("languages/" + langFileName, false);
        }
    }

    public FileConfiguration getLanguage(String lang) {
        return languages.getOrDefault(lang, languages.get(defaultLang));
    }

    public String getMessage(String lang, String path) {
        return getLanguage(lang).getString(path, "Message not found: " + path);
    }

    public Map<String, FileConfiguration> getLanguages() {
        return languages;
    }
}