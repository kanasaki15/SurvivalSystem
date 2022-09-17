package xyz.n7mn.dev.survivalsystem.customenchant.enchant;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.customenchant.CustomEnchantAbstract;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;
import xyz.n7mn.dev.survivalsystem.util.RomanNumber;

import java.util.Collections;
import java.util.Set;

public class LifeStealEnchant extends CustomEnchantAbstract {
    public LifeStealEnchant() {
        super(NamespacedKey.minecraft("life_steal"));
    }

    @Override
    public double getEnchantChance(int level) {
        return level * 0.3 + 100;
    }

    @Override
    public int getEnchantMax() {
        return 3;
    }

    @Override
    public boolean isActiveEnchant() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return true;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return ItemStackUtil.isBook(item) || ItemStackUtil.isSword(item);
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.translatable(translationKey())
                .decoration(TextDecoration.ITALIC, false)
                .color(TextColor.color(255, 20, 20))
                .append(Component.text(" " + RomanNumber.toRoman(level)));
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return EnchantmentRarity.COMMON;
    }

    @Override
    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return Collections.emptySet();
    }

    @Override
    public @NotNull String translationKey() {
        return "enchantment.custom.life_steal";
    }
}