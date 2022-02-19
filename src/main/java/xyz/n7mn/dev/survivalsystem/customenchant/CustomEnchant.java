package xyz.n7mn.dev.survivalsystem.customenchant;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
        register(CustomEnchantUtils.TEST);

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

    public boolean typeCheck(ItemStack result, ItemStack itemStack1, ItemStack itemStack2) {
        if (result != null) {
            return true;
        } else if (itemStack1 != null && itemStack2 != null) {
            return (itemStack1.getType() == itemStack2.getType()) || (itemStack2.getType() == Material.ENCHANTED_BOOK);
        } else {
            return false;
        }
    }

    public void onPrepareAnvilEvent(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();

        if (!typeCheck(event.getResult(), event.getInventory().getFirstItem(), event.getInventory().getSecondItem())) return;

        ItemStack result = pickup(event);

        if (result != null && inventory.getFirstItem() != null && inventory.getRepairCost() < inventory.getMaximumRepairCost()) {
            final AtomicBoolean atomicBoolean = new AtomicBoolean();

            final boolean hasFirstCustomEnchant = CustomEnchantUtils.hasCustomEnchant(inventory.getFirstItem());

            final boolean force = event.getView().getPlayer().getGameMode() == GameMode.CREATIVE;

            if (inventory.getSecondItem() != null) {
                final boolean hasSecondCustomEnchant = CustomEnchantUtils.hasCustomEnchant(inventory.getSecondItem());

                if (hasFirstCustomEnchant && hasSecondCustomEnchant) {
                    putEnchants(event.getInventory(), result, atomicBoolean, force);
                } else if (hasFirstCustomEnchant) {
                    notSafePutEnchants(result, inventory.getFirstItem(), false, atomicBoolean, force);
                } else if (hasSecondCustomEnchant) {
                    notSafePutEnchants(result, inventory.getSecondItem(), true, atomicBoolean, force);
                }
            } else if (event.getInventory().getRenameText() != null) {
                result.addEnchantments(inventory.getFirstItem().getEnchantments());

                atomicBoolean.set(true);
            }

            //それっぽいから適当な式
            if ((atomicBoolean.get()) || force) {
                final int cost = (inventory.getFirstItem().getRepairCost() + 1) + (inventory.getSecondItem() != null ? inventory.getSecondItem().getRepairCost() + 1 : 1);

                event.getInventory().setRepairCost(cost);

                result.setRepairCost(cost);

                event.setResult(result);
            }
        }
    }

    public void putEnchants(AnvilInventory inventory, ItemStack result, AtomicBoolean displayable, boolean force) {
        @NotNull Map<Enchantment, Integer> second = new java.util.HashMap<>(Map.copyOf(inventory.getSecondItem().getEnchantments()));

        final boolean notVanillaEnchantment =
                inventory.getFirstItem().getType() == Material.ENCHANTED_BOOK && inventory.getSecondItem().getType() == Material.ENCHANTED_BOOK
                && (!CustomEnchantUtils.hasVanillaEnchant(inventory.getFirstItem()) || !CustomEnchantUtils.hasVanillaEnchant(inventory.getSecondItem()));

        inventory.getFirstItem().getEnchants().forEach((enchantment, firstLevel) -> {

            if (enchantment instanceof CustomEnchantAbstract enchant) {

                if (enchant.canEnchantItem(result) || force) {
                    Integer level = inventory.getSecondItem().getEnchantments().get(enchant);

                    if (level != null) {
                        if (level.equals(firstLevel) && level < enchant.getMaxLevel() && enchant.isUpgradeable()) {
                            final int apply = level + 1;
                            CustomEnchantUtils.replaceLore(result, enchant.displayName(level), enchant.displayName(apply));

                            result.addEnchant(enchant, apply, true);
                        } else {
                            final int apply = Math.max(firstLevel, level);
                            CustomEnchantUtils.replaceLore(result, inventory.getFirstItem(), enchant, enchant.displayName(apply));

                            result.addEnchant(enchant, apply, true);
                        }
                        second.remove(enchant);
                    } else {
                        result.addEnchant(enchant, firstLevel, true);
                    }

                    displayable.set(true);
                }
            } else if (notVanillaEnchantment && enchantment.canEnchantItem(result)) {
                result.getItemMeta().addEnchant(enchantment, firstLevel, true);
            }
        });

        second.forEach((enchantment, level) -> {
            if (enchantment instanceof CustomEnchantAbstract enchant) {
                if (CustomEnchantUtils.addCustomEnchant(result, enchant, level, true, force)) {
                    displayable.set(true);
                }
            } else if (notVanillaEnchantment && enchantment.canEnchantItem(result)) {
                result.addEnchant(enchantment, level, true);
            }
        });
    }

    public void notSafePutEnchants(ItemStack target, ItemStack from, final boolean addLore, AtomicBoolean displayable, boolean force) {
        from.getEnchantments().forEach((enchantment, integer) -> {
            if (enchantment instanceof CustomEnchantAbstract enchant) {
                if (enchant.canEnchantItem(target) || force) {
                    target.addEnchant(enchantment, integer, true);
                    if (addLore) {
                        ItemStackUtil.addLore(target, enchant.displayName(integer));
                    }

                    displayable.set(true);
                }
            }
        });
    }

    public ItemStack pickup(PrepareAnvilEvent e) {
        if (e.getInventory().getResult() != null) {
            return e.getInventory().getResult().clone();
        } else if (e.getInventory().getFirstItem() != null && e.getInventory().getSecondItem() != null) {
            return e.getInventory().getFirstItem().clone();
        }

        return null;
    }
}