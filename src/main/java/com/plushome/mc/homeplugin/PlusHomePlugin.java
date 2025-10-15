package com.plushome.mc.homeplugin;

import com.plushome.mc.homeplugin.command.CommandManager;
import com.plushome.mc.homeplugin.config.ConfigManager;
import com.plushome.mc.homeplugin.gui.GUIManager;
import com.plushome.mc.homeplugin.storage.DatabaseManager;
import com.plushome.mc.homeplugin.teleport.TeleportManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlusHomePlugin extends JavaPlugin {

    private DatabaseManager databaseManager;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private GUIManager guiManager;
    private TeleportManager teleportManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.connect();

        commandManager = new CommandManager(this);
        commandManager.registerCommands();

        guiManager = new GUIManager(this);
        getServer().getPluginManager().registerEvents(new com.plushome.mc.homeplugin.gui.GUIListener(this), this);

        teleportManager = new TeleportManager(this);
        getServer().getPluginManager().registerEvents(new com.plushome.mc.homeplugin.teleport.TeleportListener(this), this);

        getLogger().info("plushome has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.disconnect();
        getLogger().info("plushome has been disabled!");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }
}
