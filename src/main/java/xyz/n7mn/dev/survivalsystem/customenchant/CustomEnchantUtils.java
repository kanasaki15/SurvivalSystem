package xyz.n7mn.dev.survivalsystem.customenchant;

import com.google.common.collect.ImmutableMap;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.customenchant.enchant.LifeStealEnchant;
import xyz.n7mn.dev.survivalsystem.customenchant.enchant.ResistanceEnchant;
import xyz.n7mn.dev.survivalsystem.customenchant.enchant.TestEnchant;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@UtilityClass
public class CustomEnchantUtils {
    public CustomEnchantAbstract RESISTANCE = new ResistanceEnchant();
    public CustomEnchantAbstract TEST = new TestEnchant();
    public CustomEnchantAbstract LIFE_STEAL = new LifeStealEnchant();

    public CustomEnchantAbstract[] AllEnchants = new CustomEnchantAbstract[]{
            RESISTANCE,
            TEST,
            LIFE_STEAL,
    };


    // 再起動すると謎に 空のテキストが発生します
    // 多分バグ？ だから非推奨のgetLoreを使う
    public void replaceLore(ItemStack target, Component match, Component replace) {
        if (target.hasLore()) {
            List<Component> newLore = new ArrayList<>();

            target.getItemMeta().getLore().forEach(lore -> {
                Component component = Component.text(lore);

                if (String.valueOf(match).equals(String.valueOf(component))) {
                    newLore.add(replace);
                } else {
                    newLore.add(Component.text(lore));
                }
            });

            target.lore(newLore);
        }
    }

    public void replaceLore(ItemStack target, ItemStack itemStack, Enchantment enchantment, Component replace) {
        if (target.hasLore()) {
            List<Component> newLore = new ArrayList<>();

            target.getItemMeta().getLore().forEach(lore -> {
                Component component = Component.text(lore);

                if (String.valueOf(enchantment.displayName(getIntValue(itemStack, enchantment))).equals(String.valueOf(component))) {
                    newLore.add(replace);
                } else {
                    newLore.add(Component.text(lore));
                }
            });

            target.lore(newLore);
        }
    }

    public int getIntValue(ItemStack itemStack, Enchantment enchantment) {
        if (enchantment != null) {
            if (itemStack.hasEnchant(enchantment)) return itemStack.getEnchantments().get(enchantment);
        } else {
            for (CustomEnchantAbstract enchant : CustomEnchantUtils.AllEnchants) {
                if (itemStack.hasEnchant(enchant)) return itemStack.getEnchantments().get(enchant);
            }
        }
        return 0;
    }

    public ImmutableMap<CustomEnchantAbstract, Integer> getAllCustomEnchants(ItemStack itemStack) {
        ImmutableMap.Builder<CustomEnchantAbstract, Integer> enchantment = ImmutableMap.builder();

        Map<Enchantment, Integer> enchants = itemStack.getEnchantments();

        enchants.forEach((enchant, integer) -> {
            if (enchant instanceof CustomEnchantAbstract customEnchant) {
                enchantment.put(customEnchant, integer);
            }
        });

        return enchants.size() != 0 ? enchantment.build() : ImmutableMap.of();
    }

    public ItemStack removeLore(ItemStack target, CustomEnchantAbstract enchantment, boolean cursed) {
        target.getEnchants().forEach((enchant, level) -> {
            if (enchant.equals(enchantment) && target.hasLore()) {
                removeLore(target, enchantment, level, cursed);
            }
        });
        return target;
    }

    private void removeLore(ItemStack target, CustomEnchantAbstract enchantment, final int level, final boolean cursed) {
        if (!cursed || !enchantment.isCursed()) {
            target.setLore(target.getLore().stream()
                    .filter(lore -> !lore.equals(enchantment.displayNameToString(level)))
                    .collect(Collectors.toList()));
        } else {
            target.addEnchant(enchantment, level, true);
        }
    }

    public ItemStack removeLore(ItemStack target, ItemStack from, final boolean cursed) {
        from.getEnchants().forEach((enchant, level) -> {
            if (enchant instanceof CustomEnchantAbstract found && target.hasLore()) {
                removeLore(target, found, level, cursed);
            }
        });
        return target;
    }

    public ItemStack removeLore(ItemStack target) {
        return removeLore(target, target, true);
    }

    public boolean hasCustomEnchant(ItemStack itemStack) {
        return Arrays.stream(CustomEnchantUtils.AllEnchants).anyMatch(itemStack::hasEnchant);
    }

    public boolean hasVanillaEnchant(ItemStack itemStack) {
        return itemStack.getEnchantments().keySet().stream().anyMatch(enchantment -> !(enchantment instanceof CustomEnchantAbstract));
    }

    public boolean addCustomEnchant(ItemStack itemStack, CustomEnchantAbstract enchant, int level, boolean ignoreLevelRestriction) {
        return addCustomEnchant(itemStack, enchant, level, ignoreLevelRestriction, true);
    }

    public boolean addCustomEnchant(ItemStack itemStack, CustomEnchantAbstract enchant, int level, boolean ignoreLevelRestriction, boolean force) {
        if (force || enchant.canEnchantItem(itemStack)) {
            ItemStackUtil.addLore(itemStack, enchant.displayName(level));

            itemStack.addEnchant(enchant, level, ignoreLevelRestriction);

            return true;
        }
        return false;
    }

    /**
     * @deprecated - don't need this
     */
    @Deprecated
    Collector<String, ?, List<Component>> toComponent = Collector.of(
            ArrayList::new,
            (components, s) -> components.add(Component.text(s)),
            (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            },
            Collector.Characteristics.IDENTITY_FINISH);
}