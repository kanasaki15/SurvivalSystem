package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;

public class CustomCraftTest1 extends CustomCraftAbstract {

    @Override
    public void run() {

    }

    @Override
    public CustomCraftData create() {

        CustomCraftData data = new CustomCraftData();

        ItemStack itemStack = new ItemStack(Material.GRASS);
        ItemStack itemStack1 = new ItemStack(Material.GRASS_BLOCK);
        ItemStack itemStack2 = new ItemStack(Material.GHAST_TEAR);

        // 1 2 3
        // 4 5 6
        // 7 8 9

        data.setItemData(new ItemData(itemStack), 1, 2, 3);
        data.setItemData(new ItemData(itemStack1), 4, 5, 6);
        data.setItemData(new ItemData(itemStack2), 7, 8, 9);

        return data;
    }
}