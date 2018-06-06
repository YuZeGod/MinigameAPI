package net.afyer.minigameapi.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PageInventoryHolder implements InventoryHolder
{
    private Inventory inventory;
    private int page;

    public PageInventoryHolder(int page)
    {
        this.page = page;
    }

    @Override public Inventory getInventory()
    {
        return inventory;
    }

    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
    }

    public int getPage()
    {
        return page;
    }
}
