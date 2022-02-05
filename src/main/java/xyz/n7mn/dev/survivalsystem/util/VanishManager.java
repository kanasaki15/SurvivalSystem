package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class VanishManager {
    public final List<UUID> vanishList = new ArrayList<>();

    public boolean seeVanishPlayer(Player player) {
        return player.isOp() || player.hasPermission("minecraft.op");
    }

    public boolean handleHide(Player player) {
        if (!seeVanishPlayer(player)) {
            hidePlayer(player);
        } else if (hasData(player)) {
            hidePlayer(Bukkit.getOnlinePlayers(), player);
            return false;
        }
        return true;
    }

    public void hidePlayer(Player player) {
        for (UUID vanish : vanishList) {
            Player vanishPlayer = Bukkit.getPlayer(vanish);

            //don't execute offline players
            if (vanishPlayer != null) {
                player.hidePlayer(SurvivalInstance.INSTANCE.getPlugin(), vanishPlayer);
            }
        }
    }

    public void hidePlayer(Player player, Player hidePlayer) {
        player.hidePlayer(SurvivalInstance.INSTANCE.getPlugin(), hidePlayer);
    }

    public void showPlayer(Player showPlayer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(SurvivalInstance.INSTANCE.getPlugin(), showPlayer);
        }
    }

    public void hidePlayer(Collection<? extends Player> collection, Player hidePlayer) {
        collection.forEach(player -> hidePlayer(player, hidePlayer));
    }

    public void remove(Player player) {
        vanishList.remove(player.getUniqueId());
    }

    public void add(Player player) {
        vanishList.add(player.getUniqueId());
    }

    public boolean hasData(Player player) {
        return vanishList.stream().anyMatch(uuid -> player.getUniqueId().equals(uuid));
    }
}