package xyz.n7mn.dev.survivalsystem.customcraft.item;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.advancement.base.check.ItemCheck;
import xyz.n7mn.dev.survivalsystem.util.SyncUtil;

public class ResistanceRing extends ItemCheck {
    @Override
    public void item(Player player, ItemStack itemStack) {
        if (itemStack.getPersistentDataContainer().has(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "resistance_ring"), PersistentDataType.INTEGER)
                && itemStack.getPersistentDataContainer().get(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "resistance_ring"), PersistentDataType.INTEGER) == 1
                && player.getHealth() <= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2) {

            SyncUtil.addPotionEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 0));
        }
    }
}