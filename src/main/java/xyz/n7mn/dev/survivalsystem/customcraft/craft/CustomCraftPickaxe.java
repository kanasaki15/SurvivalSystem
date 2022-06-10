package xyz.n7mn.dev.survivalsystem.customcraft.craft;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;

public class CustomCraftPickaxe extends CustomCraftAbstract {

    @Override
    public String getRecipeID() {
        return "great_pickaxe";
    }

    @Override
    public ItemData getItem(CustomCraftData data) {
        if (data != null) {
            ItemData pickaxe = ItemDataUtils.DIAMOND_PICKAXE.clone();

            pickaxe.getItemStack().addEnchantments(data.getRecipe5().getItemStack().getEnchantments());

            return pickaxe;
        } else {
            return ItemDataUtils.DIAMOND_PICKAXE;
        }
    }

    @Override
    public CustomCraftData getUsesItem() {
        return create();
    }

    @Override
    public CustomCraftData create() {
        CustomCraftData data = new CustomCraftData();

        ItemStack diamondPickAxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemStack diamondBlock = new ItemStack(Material.DIAMOND_BLOCK);

        data.setItemData(diamondBlock, 1, 2, 3, 4, 6, 7, 8, 9);
        data.setItemData(diamondPickAxe, 5);

        return data;
    }
}