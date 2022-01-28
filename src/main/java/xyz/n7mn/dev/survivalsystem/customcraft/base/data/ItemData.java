package xyz.n7mn.dev.survivalsystem.customcraft.base.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@Getter @Setter
public class ItemData {

    private ItemStack itemStack;

    private boolean checkDurability = false;
    private boolean checkMeta = false;

    private NamespacedKey namespacedKey;

    public ItemData(ItemStack itemStack) {
        if (itemStack != null) {
            this.itemStack = itemStack;
            if (itemStack.getItemMeta() != null) {
                for (NamespacedKey namespacedKey : itemStack.getPersistentDataContainer().getKeys()) {
                    this.namespacedKey = namespacedKey;

                    break;
                }
            }
        } else {
            this.itemStack = ItemDataUtils.AIR_DUMMY.itemStack;
        }
    }

    public ItemData(NamespacedKey namespacedKey, ItemStack itemStack) {
        this.itemStack = itemStack;
        this.namespacedKey = namespacedKey;
    }

    public boolean equals(ItemData data, boolean checkMeta, boolean checkDurability) {
        this.checkMeta = checkMeta;
        this.checkDurability = checkDurability;

        return equals(data);
    }


    public boolean equals(ItemData data) {
        if (checkDurability && data.getItemStack().getDurability() != itemStack.getDurability()) return false;

        if (checkMeta && (data.getItemStack().hasItemMeta() != getItemStack().hasItemMeta() || !Bukkit.getItemFactory().equals(data.getItemStack().getItemMeta(), getItemStack().getItemMeta()))) return false;
        if (!checkMeta && data.getItemStack().getType() != itemStack.getType()) return false;

        if (namespacedKey == null && data.getNamespacedKey() == null) return true;

        if (namespacedKey == null || data.getNamespacedKey() == null) return false;

        return data.getNamespacedKey().getKey().equals(namespacedKey.getKey());
    }

    public boolean hasEnchant(ItemData data, Enchantment... enchantments) {
        return equals(data) && Arrays.stream(enchantments).allMatch(enchantment -> data.getItemStack().hasEnchant(enchantment));
    }
}