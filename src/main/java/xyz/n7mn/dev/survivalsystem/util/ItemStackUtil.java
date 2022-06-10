package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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

    public ItemStack createItem(final Material material, final int amount, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);

        item.setAmount(amount);
        item.setLore(Arrays.asList(lore));

        return item;
    }

    public ItemStack createItem(final Material material, final String name, final List<String> lore) {
        final ItemStack item = new ItemStack(material, 1);

        item.setDisplayName(name);
        item.setLore(lore);

        return item;
    }

    public <T, Z> ItemStack createItem(final Material material, final String name, NamespacedKey namespacedKey, PersistentDataType<T, Z> type, Z value, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);

        item.setDisplayName(name);
        item.setLore(Arrays.asList(lore));

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.getPersistentDataContainer().set(namespacedKey, type, value);

        item.setItemMeta(itemMeta);

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

    public void addItem(HumanEntity player, ItemStack... itemStack) {
        if (player instanceof Player p) {
            addItem(p, itemStack);
        }
    }

    public boolean isArmor(ItemStack itemStack) {
        return isArmorHelmet(itemStack) || isArmorChestPlate(itemStack) || isArmorLeggings(itemStack) || isArmorBoots(itemStack);
    }

    public boolean isArmorHelmet(ItemStack itemStack) {
        return itemStack.getType().name().toLowerCase().endsWith("helmet");
    }

    public boolean isArmorChestPlate(ItemStack itemStack) {
        return itemStack.getType().name().toLowerCase().endsWith("chestplate");
    }

    public boolean isArmorLeggings(ItemStack itemStack) {
        return itemStack.getType().name().toLowerCase().endsWith("leggings");
    }

    public boolean isSword(ItemStack itemStack) {
        return itemStack.getType().name().toLowerCase().endsWith("sword");
    }

    public boolean isBook(ItemStack itemStack) {
        return itemStack.getType() == Material.ENCHANTED_BOOK;
    }

    public boolean isArmorBoots(ItemStack itemStack) {
        return itemStack.getType().name().toLowerCase().endsWith("boots");
    }

    public void addLore(ItemStack itemStack, Component... component) {
        if (itemStack.hasLore()) {
            List<Component> components = itemStack.lore();

            components.addAll(List.of(component));
            itemStack.lore(components);
        } else {
            itemStack.lore(List.of(component));
        }
    }
}