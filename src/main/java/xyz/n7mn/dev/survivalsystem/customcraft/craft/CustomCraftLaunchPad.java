package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import org.bukkit.Material;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;

public class CustomCraftLaunchPad extends CustomCraftAbstract {

    @Override
    public String getRecipeID() {
        return "mt_launch_pad";
    }

    @Override
    public ItemData getItem(CustomCraftData data) {
        return ItemDataUtils.LAUNCH_PAD;
    }

    @Override
    public CustomCraftData getUsesItem() {
        return create();
    }

    @Override
    public CustomCraftData create() {
        CustomCraftData data = new CustomCraftData();
        data.setItemData(Material.IRON_INGOT, 1, 3, 7, 9);
        data.setItemData(Material.REDSTONE, 2, 4, 6, 8);
        data.setItemData(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 5);

        return data;
    }
}
