package xyz.n7mn.dev.survivalsystem.customcraft;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ShapedRecipe;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftPickaxe;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftResistanceRing1;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftResistanceRing2;
import xyz.n7mn.dev.survivalsystem.customcraft.craft.CustomCraftTest1;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class CustomCraft {

    private final HashMap<String, CustomCraftAbstract> craftAbstractHashMap = new HashMap<>();

    public void init() {
        add(new CustomCraftTest1());
        add(new CustomCraftResistanceRing1());
        add(new CustomCraftResistanceRing2());
        add(new CustomCraftPickaxe());

        AtomicInteger count = new AtomicInteger();

        Bukkit.recipeIterator().forEachRemaining(recipeIterator -> {
            if (recipeIterator instanceof ShapedRecipe recipe) {
                CustomCraftData customCraftData = new CustomCraftData();

                recipe.getIngredientMap().values().forEach(action -> {
                    if (action != null) customCraftData.setItemData(new ItemData(action));
                    else customCraftData.setItemData(ItemDataUtils.AIR_DUMMY);
                });

                CustomCraftAbstract craftAbstract = new CustomCraftAbstract() {
                    @Override
                    public String getRecipeID() {
                        return "vanilla-" + (craftAbstractHashMap.size() + 1);
                    }

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
                craftAbstract.setShow(false);

                add(craftAbstract);

                count.getAndIncrement();
            }
            Bukkit.getConsoleSender().sendMessage("Register Vanilla Recipes..! " + recipeIterator.getResult().getType());
        });

        Bukkit.getLogger().info("Complete Register Vanilla Recipes!:" + count);
    }

    public void add(CustomCraftAbstract data) {
        craftAbstractHashMap.put(data.getRecipeID(), data);
    }
}