package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;

public class CustomCraftLeather extends CustomCraftAbstract {
    @Override
    public String getRecipeID() {
        return "mt_leather";
    }

    @Override
    public ItemData getItem(CustomCraftData data) {
        return new ItemData(new ItemStack(Material.LEATHER));
    }

    @Override
    public CustomCraftData getUsesItem() {
        return create();
    }

    @Override
    public CustomCraftData create() {
        CustomCraftData data = new CustomCraftData();

        data.setItemData(new ItemData(new ItemStack(Material.ROTTEN_FLESH)), 1, 2, 3, 4, 5, 6, 7, 8, 9);

        return data;
    }
}