package xyz.n7mn.dev.survivalsystem.customcraft.base;

import org.bukkit.event.inventory.PrepareItemCraftEvent;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;

public abstract class CustomCraftAbstract {
    public abstract ItemData getItem();

    public abstract CustomCraftData getUsesItem();

    public abstract CustomCraftData create();
}