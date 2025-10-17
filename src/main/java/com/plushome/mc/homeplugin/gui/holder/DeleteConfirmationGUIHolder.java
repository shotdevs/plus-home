package com.plushome.mc.homeplugin.gui.holder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DeleteConfirmationGUIHolder implements InventoryHolder {
    private final String homeName;
    private Inventory inventory;

    public DeleteConfirmationGUIHolder(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}