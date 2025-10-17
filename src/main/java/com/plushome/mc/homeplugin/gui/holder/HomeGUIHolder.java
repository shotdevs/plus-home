package com.plushome.mc.homeplugin.gui.holder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class HomeGUIHolder implements InventoryHolder {
    private Inventory inventory;

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}