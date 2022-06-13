package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import org.bukkit.Material;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;

public class CustomCraftResistanceRing1 extends CustomCraftAbstract {

    @Override
    public String getRecipeID() {
        return "resistance_ring_1";
    }

    @Override
    public ItemData getItem(CustomCraftData data) {
        return ItemDataUtils.RESISTANCE_RING1;
    }

    @Override
    public CustomCraftData getUsesItem() {
        return create();
    }

    @Override
    public CustomCraftData create() {
        CustomCraftData data = new CustomCraftData();

        data.setItemData(Material.YELLOW_STAINED_GLASS_PANE, 1, 2, 3, 4, 6, 7, 8, 9);
        data.setItemData(Material.DRAGON_EGG, 5);

        return data;
    }
}