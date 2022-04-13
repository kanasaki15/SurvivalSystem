package xyz.n7mn.dev.survivalsystem.customcraft.base;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CustomCraftData {

    //TODO: 正直まとめたほうがよさそう
    private ItemData recipe1, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7, recipe8, recipe9;

    @Setter
    private boolean checkDurability, checkMeta, checkPersistentData;

    public void setItemData(ItemData itemData, int... t) {
        for (int i : t) {
            setItemData(itemData, i);
        }
    }

    public void setItemData(Material material, int... t) {
        for (int i : t) {
            setItemData(new ItemData(material), i);
        }
    }

    public void setItemData(ItemStack itemStack, int... t) {
        for (int i : t) {
            setItemData(new ItemData(itemStack), i);
        }
    }

    public void setItemData(ItemData itemData) {
        List<ItemData> itemDataList = getItemData();

        for (int i = 0; i < 9; i++) {
            if (itemDataList.get(i) == null) {
                setItemData(itemData, i + 1);
                break;
            }
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
            hashMap.put(i + 1, getItemData().get(i));
        }

        return hashMap;
    }

    public boolean equals(CustomCraftData data, boolean amountCheck) {
        for (int i = 0; i < 9; i++) {
            if (!equals(data, i) || (amountCheck && nullCheck(data.getItemData().get(i)).getItemStack().getAmount() > nullCheck(getItemData().get(i)).getItemStack().getAmount())) return false;
        }
        return true;
    }

    public boolean equals(CustomCraftData data, int count) {
        return nullCheck(data.getItemData().get(count)).equals(nullCheck(getItemData().get(count)), data.checkMeta, data.checkDurability, data.checkPersistentData);
    }

    public ItemData nullCheck(ItemData itemData) {
        if (itemData == null) return ItemDataUtils.AIR_DUMMY;
        else return itemData;
    }
}