package xyz.n7mn.dev.survivalsystem.customcraft.base;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CustomCraftData {

    private ItemData recipe1, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7, recipe8, recipe9;

    private ItemData itemData = new ItemData(new ItemStack(Material.AIR));

    public void setItemData(ItemData itemData, int... t) {
        for (int i : t) {
            setItemData(itemData, i);
        }
    }

    private void setItemData(ItemData itemData, int t) {
        switch (t) {
            case 1 -> recipe1 = itemData;
            case 2 -> recipe2 = itemData;
            case 3 -> recipe3 = itemData;
            case 4 -> recipe4 = itemData;
            case 5 -> recipe5 = itemData;
            case 6 -> recipe6 = itemData;
            case 7 -> recipe7 = itemData;
            case 8 -> recipe8 = itemData;
            case 9 -> recipe9 = itemData;
        }
    }

    public List<ItemData> getItemData() {
        return Arrays.asList(recipe1, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7, recipe8, recipe9);
    }

    public Map<Integer, ItemData> getHashMap() {
        Map<Integer, ItemData> hashMap = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            hashMap.put(i, getItemData().get(i));
        }

        return hashMap;
    }

    public boolean equals(CustomCraftData data) {
        return getItemData().stream().allMatch(i -> data.getItemData().stream().anyMatch(itemData -> {
            ItemData item = i != null ? i : itemData;

            return item.equals(i != null ? i : data);
        }));
    }
}