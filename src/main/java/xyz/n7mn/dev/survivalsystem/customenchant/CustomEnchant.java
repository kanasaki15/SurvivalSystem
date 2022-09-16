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

        register(CustomEnchantUtils.RESISTANCE);
        //register(CustomEnchantUtils.TEST);
        register(CustomEnchantUtils.LIFE_STEAL);
        register(CustomEnchantUtils.NIGHT_VISION);
        register(CustomEnchantUtils.KINETIC_RESISTANCE);

        Enchantment.stopAcceptingRegistrations();
    }

    public void register(Enchantment enchantment) {
        if (Enchantment.getByKey(enchantment.getKey()) == null) {
            Enchantment.registerEnchantment(enchantment);
        }
    }

    public void onPrepareGrindstoneEvent(GrindstoneInventory inventory) {
        ItemStack itemStack = inventory.getResult();

        if (itemStack != null) {
            if (itemStack.getType() != Material.BOOK) {
                if (inventory.getUpperItem() != null) {
                    CustomEnchantUtils.removeLore(itemStack, inventory.getUpperItem(), true);
                }

                if (inventory.getLowerItem() != null) {
                    CustomEnchantUtils.removeLore(itemStack, inventory.getLowerItem(), true);
                }
            } else {
                //TODO:
            }

            //WHY?!??!
            Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> inventory.setResult(itemStack));
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
            addEnchantment(player, enchants, inv.getSecondItem(), inv.getFirstItem());
            addEnchantment(player, enchants, inv.getFirstItem(), inv.getSecondItem());

            item = inv.getFirstItem().clone();

            for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                if (entry.getKey() instanceof CustomEnchantAbstract customEnchant) {
                    CustomEnchantUtils.removeLore(item, customEnchant, customEnchant.isCursed());
                }
            }

            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof CustomEnchantAbstract customEnchant) {
                    CustomEnchantUtils.addCustomEnchant(item, customEnchant, entry.getValue(), true, true);
                } else {
                    item.addEnchant(entry.getKey(), entry.getValue(), true);
                }
            }
        }

        return item;
    }

    public void addEnchantment(HumanEntity player, Map<Enchantment, Integer> enchants, ItemStack item, ItemStack target) {
        for (Map.Entry<Enchantment, Integer> enchantment : item.getEnchantments().entrySet()) {
            if (!enchantment.getKey().canEnchantItem(target) && player.getGameMode() != GameMode.CREATIVE) {
                continue;
            }
            //?
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