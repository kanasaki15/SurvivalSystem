package xyz.n7mn.dev.survivalsystem.customcraft.base.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@Getter @Setter
public class ItemData {

    private ItemStack itemStack;

    private boolean checkDurability = false;

    private NamespacedKey namespacedKey;

    public ItemData(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemData(NamespacedKey namespacedKey, ItemStack itemStack) {
        this.itemStack = itemStack;
        this.namespacedKey = namespacedKey;
    }

    public boolean equals(ItemData data) {
        if (checkDurability && data.getItemStack().getDurability() != itemStack.getMaxItemUseDuration()) return false;

        if (data.getItemStack().getType() != itemStack.getType()) return false;

        if (namespacedKey == null && data.getNamespacedKey() == null) return true;

        return data.getNamespacedKey().getKey().equals(namespacedKey.getKey());
    }

    public boolean hasEnchant(ItemData data, Enchantment... enchantments) {
        return equals(data) && Arrays.stream(enchantments).allMatch(enchantment -> data.getItemStack().hasEnchant(enchantment));
    }

    public boolean equalsMeta(ItemData data) {
        return data.getItemStack().getItemMeta().equals(itemStack.getItemMeta());
    }
}