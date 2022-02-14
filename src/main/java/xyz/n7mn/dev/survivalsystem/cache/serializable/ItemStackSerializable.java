package xyz.n7mn.dev.survivalsystem.cache.serializable;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;


@SuppressWarnings("deprecation")
public class ItemStackSerializable {

    @NotNull
    public static ItemStackData serialize(ItemStack itemStack) {
        @NotNull Map<String, Object> object = itemStack.getItemMeta().serialize();

        return new ItemStackData(translate(itemStack), object);
    }

    /**
     * @see ItemStack
     * taken from Spigot/Bukkit ItemStack
     */
    private static Map<String, Object> translate(ItemStack itemStack) {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("v", Bukkit.getUnsafe().getDataVersion()); // Include version to indicate we are using modern material names (or LEGACY prefix)
        result.put("type", itemStack.getType().name());

        if (itemStack.getAmount() != 1) {
            result.put("amount", itemStack.getAmount());
        }

        return result;
    }

    /**
     * @see ItemStack
     * taken from Spigot/Bukkit ItemStack
     */
    @NotNull
    public static ItemStack deserialize(@NotNull Map<String, Object> itemStack, Map<String, Object> itemMeta) {
        int version = (itemStack.containsKey("v")) ? ((Number) itemStack.get("v")).intValue() : -1;
        short damage = 0;
        int amount = 1;

        if (itemStack.containsKey("damage")) {
            damage = ((Number) itemStack.get("damage")).shortValue();
        }

        Material type;
        if (version < 0) {
            type = Material.getMaterial(Material.LEGACY_PREFIX + itemStack.get("type"));

            byte dataVal = (type != null && type.getMaxDurability() == 0) ? (byte) damage : 0; // Actually durable items get a 0 passed into conversion
            type = Bukkit.getUnsafe().fromLegacy(new MaterialData(type, dataVal), true);

            // We've converted now so the data val isn't a thing and can be reset
            if (dataVal != 0) {
                damage = 0;
            }
        } else {
            type = Bukkit.getUnsafe().getMaterial((String) itemStack.get("type"), version);
        }

        if (itemStack.containsKey("amount")) {
            amount = ((Number) itemStack.get("amount")).intValue();
        }

        ItemStack result = new ItemStack(type, amount, damage);

        if (translate(itemMeta) instanceof ItemMeta meta) {
            result.setItemMeta(meta);
        }

        if (itemMeta.containsKey("enchants")) { // Backward compatiblity, @deprecated
            Object raw = itemMeta.get("enchants");

            if (raw instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());

                    //gsonのバグ？でdoubleで判定しないといけません！
                    if ((enchantment != null) && (entry.getValue() instanceof Double value)) {
                        result.addUnsafeEnchantment(enchantment, value.intValue());
                    }
                }
            }
        }

        if (version < 0) {
            // Set damage again incase meta overwrote it
            if (itemStack.containsKey("damage")) {
                result.setDurability(damage);
            }
        }

        return result.ensureServerConversions(); // Paper
    }

    /**
     * protectedで保護されているので愚直に判定を入れる
     */
    private static Object translate(@NotNull Map<String, Object> args) {
        ImmutableMap<String,Object> map = ImmutableMap.<String, Object>builder()
                .putAll(args)
                .put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, "ItemMeta")
                .build();

        return ConfigurationSerialization.deserializeObject(map);
    }
}