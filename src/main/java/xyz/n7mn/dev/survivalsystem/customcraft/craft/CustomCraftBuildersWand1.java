package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import org.bukkit.Material;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;

public class CustomCraftBuildersWand1 extends CustomCraftAbstract {

    @Override
    public String getRecipeID() {
        return "builders_wand_1";
    }

    @Override
    public ItemData getItem(CustomCraftData data) {
        return ItemDataUtils.BUILDERS_WAND_1;
    }

    @Override
    public CustomCraftData getUsesItem() {
        return create();
    }

    @Override
    public CustomCraftData create() {
        CustomCraftData data = new CustomCraftData();

        data.setItemData(Material.STICK, 5, 8);
        data.setItemData(Material.DIAMOND_BLOCK, 1, 3);
        data.setItemData(Material.GOLD_BLOCK, 2, 4, 6);

        return data;
    }
}