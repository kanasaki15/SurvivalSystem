package xyz.n7mn.dev.survivalsystem.customenchant;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CustomEnchant {
    //TODO: 村人のトレードも対応しますか？

    public void init() {
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true); //privateなのでtrueに変更
            field.setBoolean(null, true);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //register(CustomEnchantUtils.RESISTANCE);
        //register(CustomEnchantUtils.TEST);
        register(CustomEnchantUtils.LIFE_STEAL);
        register(CustomEnchantUtils.NIGHT_VISION);
        //register(CustomEnchantUtils.KINETIC_RESISTANCE);

        Enchantment.stopAcceptingRegistrations();
    }

    public void register(Enchantment enchantment) {
        if (Enchantment.getByKey(enchantment.getKey()) == null) {
            Enchantment.registerEnchantment(enchantment);
        }
    }

    public void onPrepareGrindstoneEvent(GrindstoneInventory inventory) {
        ItemStack item = inventory.getResult();

        //true
        if (item != null) {
            Map<Enchantment, Integer> cursedEnchants = new HashMap<>();

            if (inventory.getLowerItem() != null) {
                prepare(inventory.getLowerItem(), cursedEnchants);
            }

            if (inventory.getUpperItem() != null) {
                prepare(inventory.getUpperItem(), cursedEnchants);
            }

            if (item.getType() == Material.BOOK && !cursedEnchants.isEmpty()) {
                item.setType(Material.ENCHANTED_BOOK);
                cursedEnchants.forEach((enchantment, level) -> CustomEnchantUtils.addEnchant(item, enchantment, level, true, true));
            } else {
                cursedEnchants.forEach((enchantment, level) -> item.addEnchant(enchantment, level, true));
            }

            //I don't know how to work
            Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> {
                inventory.setResult(item);
            });
        }
    }

    public void prepare(ItemStack item, Map<Enchantment, Integer> cursedEnchants) {
        Map<Enchantment, Integer> enchants = item.getType() == Material.ENCHANTED_BOOK ?
                ((EnchantmentStorageMeta) item.getItemMeta()).getStoredEnchants() : item.getEnchantments();

        for (Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            if (enchant.getKey().isCursed()) {
                cursedEnchants.put(enchant.getKey(), enchant.getValue());
            } else {
                CustomEnchantUtils.removeLore(item, enchant.getKey());
            }
        }
    }

    public void onPrepareAnvilEvent(PrepareAnvilEvent event) {
        ItemStack t = putEnchants(event.getView().getPlayer(), event.getInventory());

        if (t != null) {
            event.setResult(t);
        }
    }

    public ItemStack putEnchants(final HumanEntity player, AnvilInventory inv) {
        Map<Enchantment, Integer> enchants = new HashMap<>();

        ItemStack item = inv.getResult();

        //its rename text
        if (inv.getResult() != null && inv.getFirstItem() != null && inv.getSecondItem() == null) {
            addEnchantment(player, enchants, inv.getResult(), inv.getFirstItem());

            inv.getResult().addEnchantments(enchants);
        } else if (inv.getFirstItem() != null && inv.getSecondItem() != null) {
            //cannot enchantment!
            final boolean isSecondBook = inv.getFirstItem().getType() != Material.ENCHANTED_BOOK && inv.getSecondItem().getType() == Material.ENCHANTED_BOOK;

            if (inv.getResult() == null && !isSecondBook) {
                return null;
            }

            if (!isSecondBook && inv.getFirstItem().getType() != inv.getSecondItem().getType()) {
                return null;
            }

            addEnchantment(player, enchants, inv.getSecondItem(), inv.getFirstItem());
            addEnchantment(player, enchants, inv.getFirstItem(), inv.getSecondItem());

            Map<Enchantment, Integer> enchantments = inv.getFirstItem().getType() == Material.ENCHANTED_BOOK
                    ? ((EnchantmentStorageMeta) inv.getFirstItem().getItemMeta()).getStoredEnchants() : inv.getFirstItem().getEnchantments();

            //don't accept not valid enchant
            if (inv.getResult() == null && isSameEnchants(enchantments, enchants)) {
                return null;
            }

            item = inv.getFirstItem().clone();

            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                if (entry.getKey() instanceof CustomEnchantAbstract customEnchant) {
                    //safe remove lore!
                    CustomEnchantUtils.removeLore(item, customEnchant);
                }
            }

            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                CustomEnchantUtils.addEnchant(item, entry.getKey(), entry.getValue(), true, entry.getKey() instanceof CustomEnchantAbstract);
            }

            if (inv.getRenameText() != null) {
                item.setDisplayName(inv.getRenameText());
            }
        }

        return item;
    }

    public boolean isSameEnchants(Map<Enchantment, Integer> map1, Map<Enchantment, Integer> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        Map<Enchantment, Integer> map = new HashMap<>(map1);
        map2.forEach(map::remove);

        return map.isEmpty();
    }

    public void addEnchantment(HumanEntity player, Map<Enchantment, Integer> enchants, ItemStack item, ItemStack target) {
        addEnchantment(player, item, target, enchants, item.getType() == Material.ENCHANTED_BOOK
                ? ((EnchantmentStorageMeta) item.getItemMeta()).getStoredEnchants() : item.getEnchantments());
    }

    private void addEnchantment(HumanEntity entity, ItemStack item, ItemStack target, Map<Enchantment, Integer> enchants, Map<Enchantment, Integer> enchantments) {
        //?
        final boolean canSkip = (item.getType() == Material.ENCHANTED_BOOK && target.getType() == Material.ENCHANTED_BOOK) || entity.getGameMode() == GameMode.CREATIVE;

        for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
            if (!canSkip && !enchantment.getKey().canEnchantItem(item) && !enchantment.getKey().canEnchantItem(target)) {
                continue;
            }
            final Integer level = enchants.get(enchantment.getKey());

            if (level == null) {
                enchants.put(enchantment.getKey(), enchantment.getValue());
            } else if (level.equals(enchantment.getValue())) {
                enchants.put(enchantment.getKey(), enchantment.getValue() < enchantment.getKey().getMaxLevel() ? level + 1 : level);
            } else if (enchantment.getValue() > level) {
                enchants.put(enchantment.getKey(), enchantment.getValue());
            }
        }
    }
}