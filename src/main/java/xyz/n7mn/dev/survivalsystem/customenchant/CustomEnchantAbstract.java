package xyz.n7mn.dev.survivalsystem.customenchant;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class CustomEnchantAbstract extends Enchantment {

    private boolean upgradeable = true;

    public CustomEnchantAbstract(@NotNull NamespacedKey key) {
        super(key);
    }

    public abstract double getEnchantChance(final int level);

    public abstract int getEnchantMax();

    public abstract boolean isActiveEnchant();

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return Collections.emptySet();
    }

    public boolean isUpgradeable() {
        return upgradeable;
    }

    public void setUpgradeable(boolean upgradeable) {
        this.upgradeable = upgradeable;
    }

    @Override
    public @NotNull String getName() {
        return  getName(1);
    }

    public @NotNull String getName(int level) {
        return GsonComponentSerializer.gson().serialize(displayName(level));
    }
}