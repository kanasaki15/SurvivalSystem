package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;

public class CustomCraftResistanceRing2 extends CustomCraftAbstract {
    @Override
    public String getRecipeID() {
        return "resistance_ring_2";
    }

    @Override
    public ItemData getItem(CustomCraftData data) {
        return ItemDataUtils.RESISTANCE_RING2;
    }

    @Override
    public CustomCraftData getUsesItem() {
        return create();
    }

    @Override
    public CustomCraftData create() {
        CustomCraftData data = new CustomCraftData();

        data.setItemData(new ItemStack(Material.DIAMOND), 1, 2, 3, 4, 6, 7, 8, 9);
        data.setItemData(ItemDataUtils.RESISTANCE_RING1, 5);

        data.setCheckPersistentData(true);

        return data;
    }
}