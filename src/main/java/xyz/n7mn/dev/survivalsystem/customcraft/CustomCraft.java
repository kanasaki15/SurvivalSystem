package xyz.n7mn.dev.survivalsystem.customcraft;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ShapedRecipe;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftTest1;

import java.util.HashMap;

@Getter
public class CustomCraft {

    private final HashMap<String, CustomCraftAbstract> craftAbstractHashMap = new HashMap<>();

    public void init() {
        Bukkit.recipeIterator().forEachRemaining(recipeIterator -> {
            if (recipeIterator instanceof ShapedRecipe recipe) {
                CustomCraftData customCraftData = new CustomCraftData();

                recipe.getIngredientMap().values().forEach(action -> {
                    if (action != null) customCraftData.setItemData(new ItemData(action));
                    else customCraftData.setItemData(ItemDataUtils.AIR_DUMMY);
                });

                CustomCraftAbstract craftAbstract = new CustomCraftAbstract() {
                    @Override
                    public ItemData getItem(CustomCraftData data) {
                        return new ItemData(recipeIterator.getResult());
                    }

                    @Override
                    public CustomCraftData getUsesItem() {
                        return create();
                    }

                    @Override
                    public CustomCraftData create() {
                        return customCraftData;
                    }
                };
                craftAbstractHashMap.put(String.valueOf(craftAbstractHashMap.size()), craftAbstract);
            }
            Bukkit.getConsoleSender().sendMessage("Register Vanilla Recipes..! " + recipeIterator.getResult().getType());
        });

        craftAbstractHashMap.put("test-1", new CustomCraftTest1());
    }
}