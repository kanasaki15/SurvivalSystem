package xyz.n7mn.dev.survivalsystem.gui.customcraft;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class CraftHolder implements InventoryHolder {

    private final Map<Integer, GUIItem> hashMap = new HashMap<>();

    private Inventory inventory;

    private final int commandID;

    public CraftHolder(int commandID) {
        this.commandID = commandID;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void addListener(int chestID, GUIItem guiItem) {
        hashMap.put(chestID, guiItem);
    }

    public CustomCraftData translateCustomCraftData() {
        CustomCraftData customCraftData = new CustomCraftData();

        List<Integer> denyList = denyList();

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inventory.getItem(denyList.get(i));

            customCraftData.setItemData(new ItemData(itemStack), i + 1);
        }

        return customCraftData;
    }

    public List<Integer> denyList() {
        return Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
    }
}
