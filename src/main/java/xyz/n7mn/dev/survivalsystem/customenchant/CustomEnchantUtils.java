package xyz.n7mn.dev.survivalsystem.customenchant;

import com.google.common.collect.ImmutableMap;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentIteratorFlag;
import net.kyori.adventure.text.ComponentIteratorType;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import xyz.n7mn.dev.survivalsystem.customenchant.enchant.*;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

@SuppressWarnings("deprecation")
@UtilityClass
public class CustomEnchantUtils {
    //public CustomEnchantAbstract RESISTANCE = new ResistanceEnchant();
    //public CustomEnchantAbstract TEST = new TestEnchant();
    public CustomEnchantAbstract LIFE_STEAL = new LifeStealEnchant();
    public CustomEnchantAbstract NIGHT_VISION = new NightVisionEnchant();
    //public CustomEnchantAbstract KINETIC_RESISTANCE = new KineticEnergyResistanceEnchant();

    public CustomEnchantAbstract[] AllEnchants = new CustomEnchantAbstract[]{
            //RESISTANCE,
            //TEST,
            LIFE_STEAL,
            NIGHT_VISION,
           // KINETIC_RESISTANCE,
    };

    public void replaceLore(ItemStack target, Component match, Component replace) {
        if (target.hasLore()) {
            List<Component> format = new ArrayList<>();

            String mt = GsonComponentSerializer.gson().serialize(match);

            target.lore().forEach(lore -> {
                String st = GsonComponentSerializer.gson().serialize(lore);

                if (mt.equals(st)) format.add(replace);
                else format.add(lore);
            });

            //target.getItemMeta().getLore().forEach(lore -> {
            //    Component component = Component.text(lore);
            //
            //    if (String.valueOf(match).equals(String.valueOf(component))) {
            //        formatter.add(replace);
            //    } else {
            //        formatter.add(Component.text(lore));
            //    }
            //});

            target.lore(format);
        }
    }

    public void replaceLore(ItemStack target, ItemStack itemStack, Enchantment enchantment, Component replace) {
        if (target.hasLore()) {
            List<Component> format = new ArrayList<>();

            String enchantLore = GsonComponentSerializer.gson().serialize(enchantment.displayName(getIntValue(itemStack, enchantment)));

            target.lore().forEach(lore -> {
                String st = GsonComponentSerializer.gson().serialize(lore);

                if (enchantLore.equals(st)) format.add(replace);
                else format.add(lore);

            });

            //target.getItemMeta().getLore().forEach(lore -> {
            //    Component component = Component.text(lore);
            //
            //    if (String.valueOf(enchantment.displayName(getIntValue(itemStack, enchantment))).equals(String.valueOf(component))) {
            //        format.add(replace);
            //    } else {
            //        format.add(Component.text(lore));
            //    }
            //});

            target.lore(format);
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

    public ItemStack removeLore(ItemStack item, Enchantment enchantment) {
        item.getEnchants().forEach((enchant, level) -> {
            if (enchant.equals(enchantment) && item.hasLore()) {
                item.lore(removeLore(item, enchantment, level));
            }
        });
        return item;
    }

    private List<Component> removeLore(ItemStack item, Enchantment enchantment, int level) {
        List<Component> components = new ArrayList<>();

        item.getItemMeta().lore().forEach(lore -> {
            boolean skip = false;

            for (Component componentLike : lore.iterable(ComponentIteratorType.DEPTH_FIRST, ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS)) {
                if (componentLike instanceof TranslatableComponent translate && translate.key().equals(enchantment.translationKey())) {
                    skip = true;
                }
            }

            if (!skip) {
                components.add(lore);
            }
        });

        return components;
    }

    public boolean hasCustomEnchant(ItemStack itemStack) {
        return Arrays.stream(CustomEnchantUtils.AllEnchants).anyMatch(itemStack::hasEnchant);
    }

    public boolean hasVanillaEnchant(ItemStack itemStack) {
        return itemStack.getEnchantments().keySet().stream().anyMatch(enchantment -> !(enchantment instanceof CustomEnchantAbstract));
    }

    public void addCustomEnchant(ItemStack itemStack, CustomEnchantAbstract enchant, int level, boolean ignoreLevelRestriction) {
        if (itemStack.addEnchant(enchant, level, ignoreLevelRestriction)) {
            ItemStackUtil.addLore(itemStack, enchant.displayName(level));
        }
    }

    public void addEnchant(ItemStack item, Enchantment enchantment, int level, final boolean ignoreLevelRestriction, boolean addingLore) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

            meta.addStoredEnchant(enchantment, level, ignoreLevelRestriction);
            item.setItemMeta(meta);
        } else {
            item.addEnchant(enchantment, level, ignoreLevelRestriction);
        }

        if (addingLore && enchantment instanceof CustomEnchantAbstract customEnchant) {
            ItemStackUtil.addLore(item, customEnchant.displayName(level));
        }
    }

    public void addCustomEnchant(ItemStack item, CustomEnchantAbstract customEnchant, int level, boolean ignoreLevelRestriction, boolean forceAddingLore) {
        if (item.addEnchant(customEnchant, level, ignoreLevelRestriction) || forceAddingLore) {
            ItemStackUtil.addLore(item, customEnchant.displayName(level));
        }
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