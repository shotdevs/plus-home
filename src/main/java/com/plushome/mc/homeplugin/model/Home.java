package com.plushome.mc.homeplugin.model;

import org.bukkit.Location;
import java.util.UUID;

public class Home {

    private final UUID playerUUID;
    private final String homeName;
    private final Location location;

    public Home(UUID playerUUID, String homeName, Location location) {
        this.playerUUID = playerUUID;
        this.homeName = homeName;
        this.location = location;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getHomeName() {
        return homeName;
    }

    public Location getLocation() {
        return location;
    }
}
