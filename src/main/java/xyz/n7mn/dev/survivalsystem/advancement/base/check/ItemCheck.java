package xyz.n7mn.dev.survivalsystem.advancement.base.check;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

public abstract class ItemCheck {
    public abstract void item(Player player, ItemStack itemStack);

    public void grant(Player player, String advancementId) {
        grant(player, advancementId, "grant");
    }

    public void grant(Player player, String advancementId, String criteria) {
        player.getAdvancementProgress(Bukkit.getAdvancement(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), advancementId))).awardCriteria(criteria);
    }
}