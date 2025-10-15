package com.plushome.mc.homeplugin.command;

import com.plushome.mc.homeplugin.PlusHomePlugin;

public class CommandManager {

    private final PlusHomePlugin plugin;

    public CommandManager(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        plugin.getCommand("sethome").setExecutor(new SetHomeCommand(plugin));
        plugin.getCommand("delhome").setExecutor(new DelHomeCommand(plugin));
        plugin.getCommand("home").setExecutor(new HomeCommand(plugin));
    }
}
