package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;

public class CustomCraftTest2 extends CustomCraftAbstract {

    @Override
    public ItemData getItem(CustomCraftData data) {
        return null;
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

        return data;
    }
}