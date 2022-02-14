package xyz.n7mn.dev.survivalsystem.customcraft.base.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter @Setter
public class ItemData {

    private ItemStack itemStack;

    private Set<NamespacedKey> namespacedKey;

    public ItemData(ItemStack itemStack) {
        if (itemStack != null) {
            this.itemStack = itemStack;
            if (itemStack.getItemMeta() != null) {
                namespacedKey = itemStack.getPersistentDataContainer().getKeys();
            }
        } else {
            this.itemStack = ItemDataUtils.AIR_DUMMY.itemStack;
        }
    }

    public boolean equals(ItemData data, boolean checkMeta, boolean checkDurability, boolean checkPersistentData) {
        return checkEquals(data, checkMeta, checkDurability, checkPersistentData);
    }

    public boolean equalsCheckEnchant(ItemData data, Enchantment... enchantments) {
        return equals(data) && Arrays.stream(enchantments).allMatch(enchantment -> data.getItemStack().hasEnchant(enchantment));
    }


    private boolean checkEquals(ItemData data, boolean checkMeta, boolean checkDurability, boolean checkPersistentData) {
        if (checkDurability && data.getItemStack().getDurability() != itemStack.getDurability()) return false;

        if (checkMeta && (data.getItemStack().hasItemMeta() != getItemStack().hasItemMeta() || !Bukkit.getItemFactory().equals(data.getItemStack().getItemMeta(), getItemStack().getItemMeta()))) return false;
        if (!checkMeta && data.getItemStack().getType() != itemStack.getType()) return false;

        if ((namespacedKey == null && data.getNamespacedKey() == null) || (namespacedKey.isEmpty() && data.getNamespacedKey().isEmpty())) return true;

        if (namespacedKey == null || data.namespacedKey == null || namespacedKey.size() != data.getNamespacedKey().size()) return false;

        if (!data.getNamespacedKey().stream().allMatch(this::hasNamespacedKey)) return false;
        else if (!checkPersistentData) return true;

        return persistentDataContainerCheck(data);
    }

    private boolean hasNamespacedKey(NamespacedKey key) {
        return namespacedKey.stream().anyMatch(namespace -> namespace.getKey().equals(key.getKey()) && namespace.getNamespace().equals(key.getNamespace()));
    }

    private boolean persistentDataContainerCheck(ItemData itemData) {
        //これはListにしてgetするよりも早いか実験してもいいかも

        for (NamespacedKey namespacedKey : namespacedKey) {
            Object value1 = getAnyKey(itemStack, namespacedKey);

            if (value1 == null) return false;

            for (NamespacedKey find : itemData.namespacedKey) {
                if (find.equals(namespacedKey)) {

                    Object value2 = getAnyKey(itemData.getItemStack(), find);

                    if (value2 == null) return false;

                    return value1.equals(value2);
                }
            }
        }

        return false;
    }

    private List<PersistentDataType<?,?>> persistentDataTypes = Arrays.asList(PersistentDataType.BYTE,
    PersistentDataType.SHORT,
    PersistentDataType.INTEGER,
    PersistentDataType.LONG,
    PersistentDataType.FLOAT,
    PersistentDataType.DOUBLE,
    PersistentDataType.STRING,
    PersistentDataType.BYTE_ARRAY,
    PersistentDataType.INTEGER_ARRAY,
    PersistentDataType.LONG_ARRAY,
    PersistentDataType.TAG_CONTAINER_ARRAY,
    PersistentDataType.TAG_CONTAINER);

    private Object getAnyKey(ItemStack itemStack, NamespacedKey namespacedKey) {
        for (PersistentDataType<?, ?> persistentDataType : persistentDataTypes) {
            if (itemStack.getPersistentDataContainer().has(namespacedKey, persistentDataType)) {
                return itemStack.getPersistentDataContainer().get(namespacedKey, persistentDataType);
            }
        }

        return null;
    }
}