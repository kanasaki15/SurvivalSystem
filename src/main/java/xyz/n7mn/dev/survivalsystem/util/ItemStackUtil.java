package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@UtilityClass
public class ItemStackUtil {
    public ItemStack createItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);

        item.setDisplayName(name);
        item.setLore(Arrays.asList(lore));

        return item;
    }
}