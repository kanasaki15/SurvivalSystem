package xyz.n7mn.dev.survivalsystem.gui.customcraft.recipe;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIItem;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class RecipeHolder implements InventoryHolder {

    private final Map<Integer, GUIItem> hashMap = new HashMap<>();

    private Map<String, CustomCraftAbstract> craftRecipe = new HashMap<>();

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

    public void addListener(final int id, final GUIItem guiItem) {
        hashMap.put(id, guiItem);
    }
}