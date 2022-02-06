package xyz.n7mn.dev.survivalsystem.customcraft.base;

import lombok.Getter;
import lombok.Setter;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;

@Getter @Setter
public abstract class CustomCraftAbstract {
    private boolean show = true;

    public abstract String getRecipeID();

    public abstract ItemData getItem(CustomCraftData data);

    public abstract CustomCraftData getUsesItem();

    public abstract CustomCraftData create();
}