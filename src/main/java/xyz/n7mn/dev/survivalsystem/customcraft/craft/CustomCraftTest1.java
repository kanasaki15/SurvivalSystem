package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import org.bukkit.Material;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;

public class CustomCraftTest1 extends CustomCraftAbstract {

    @Override
    public String getRecipeID() {
        return null;
    }

    @Override
    public ItemData getItem(CustomCraftData data) {
        return ItemDataUtils.DIRT_HELMET;
    }

    @Override
    public CustomCraftData getUsesItem() {
        return create();
    }

    @Override
    public CustomCraftData create() {
        CustomCraftData data = new CustomCraftData();

        // 1 2 3
        // 4 5 6
        // 7 8 9

        data.setItemData(Material.GRASS, 1, 2, 3);
        data.setItemData(Material.GRASS_BLOCK, 4, 5, 6);
        data.setItemData(ItemDataUtils.AIR_DUMMY, 7, 8, 9);

        return data;
    }
}