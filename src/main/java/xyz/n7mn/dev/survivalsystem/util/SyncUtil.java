package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.util.Collection;

@UtilityClass
public class SyncUtil {
    public void addPotionEffect(Player player, PotionEffect effect) {
        Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> player.addPotionEffect(effect));
    }

    public void addPotionEffects(Player player, Collection<PotionEffect> effects) {
        Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> player.addPotionEffects(effects));
    }
}