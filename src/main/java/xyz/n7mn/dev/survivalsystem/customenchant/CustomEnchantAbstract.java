package xyz.n7mn.dev.survivalsystem.customenchant;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public abstract class CustomEnchantAbstract extends Enchantment {

    @Getter @Setter
    private boolean upgradeable = true;

    public CustomEnchantAbstract(@NotNull NamespacedKey key) {
        super(key);
    }

    public abstract String displayNameToString(final int level);

    public abstract double getEnchantChance(final int level);

    public abstract int getEnchantMax();

    public abstract boolean isActiveEnchant();
}
