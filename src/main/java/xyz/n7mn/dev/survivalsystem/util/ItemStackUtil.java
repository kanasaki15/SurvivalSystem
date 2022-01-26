package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
@UtilityClass
public class ItemStackUtil {
    public ItemStack createItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);

        item.setDisplayName(name);
        item.setLore(Arrays.asList(lore));

        return item;
    }

    public ItemStack createItem(final Material material, final String name, final List<String> lore) {
        final ItemStack item = new ItemStack(material, 1);

        item.setDisplayName(name);
        item.setLore(lore);

        return item;
    }

    public void addItem(Player player, ItemStack... itemStack) {
        for (ItemStack item : itemStack) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            } else {
                ((Item) player.getWorld().spawnEntity(player.getLocation(), EntityType.DROPPED_ITEM)).setItemStack(item);
            }
        }
    }
}