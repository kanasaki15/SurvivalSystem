package xyz.n7mn.dev.survivalsystem.customcraft.base;

import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;

public abstract class CustomCraftAbstract {
    public abstract ItemData getItem(CustomCraftData data);

    public abstract CustomCraftData getUsesItem();

    public abstract CustomCraftData create();
}