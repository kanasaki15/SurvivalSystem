package xyz.n7mn.dev.survivalsystem.cache.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Utility;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


//補足: record だとエラーが出た気がする
public class ItemStackSerializable implements Serializable {

    public ItemStackSerializable(List<Map<String, Object>> itemStack) {
        this.itemStack = itemStack;
    }

    @Serial //現在のバージョンは1...
    private static final long serialVersionUID = 1;

    private List<Map<String, Object>> itemStack;

    public List<Map<String, Object>> getSerializable() {
        return itemStack;
    }

    public void setSerializable(List<Map<String, Object>> itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    @Utility
    public static ItemStackData serialize(ItemStack itemStack) {
        return new ItemStackData(translate(itemStack), itemStack.getItemMeta().serialize());
    }

    private static Map<String, Object> translate(ItemStack itemStack) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("v", Bukkit.getUnsafe().getDataVersion()); // Include version to indicate we are using modern material names (or LEGACY prefix)
        result.put("type", itemStack.getType().name());

        if (itemStack.getAmount() != 1) {
            result.put("amount", itemStack.getAmount());
        }

        return result;
    }

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
            type = Material.getMaterial(Material.LEGACY_PREFIX + (String) itemStack.get("type"));

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

        Object object = ConfigurationSerialization.deserializeObject(itemMeta);
        if (object instanceof ItemMeta meta) {
            result.setItemMeta(meta);
        }

        if (version < 0) {
            // Set damage again incase meta overwrote it
            if (itemStack.containsKey("damage")) {
                result.setDurability(damage);
            }
        }

        return result.ensureServerConversions(); // Paper
    }
}