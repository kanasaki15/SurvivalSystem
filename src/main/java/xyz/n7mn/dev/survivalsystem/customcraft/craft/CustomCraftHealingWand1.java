package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;

public class CustomCraftHealingWand1 extends CustomCraftAbstract {

    @Override
    public String getRecipeID() {
        return "heal_wand_1";
    }

    @Override
    public ItemData getItem(CustomCraftData data) {
        return ItemDataUtils.HEALING_WAND;
    }

    @Override
    public CustomCraftData getUsesItem() {
        return create();
    }

    @Override
    public CustomCraftData create() {
        CustomCraftData data = new CustomCraftData();

        return null;
    }
}
