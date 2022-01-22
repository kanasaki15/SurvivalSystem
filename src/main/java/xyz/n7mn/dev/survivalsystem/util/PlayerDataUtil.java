package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;

import java.util.*;

@UtilityClass
public class PlayerDataUtil {
    private Map<UUID, PlayerData> playerData = new HashMap<>();

    public PlayerData getPlayerData(Player player) {
        return playerData.get(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public Collection<PlayerData> getPlayerData() {
        return playerData.values();
    }

    public void putPlayerData(Player player) {
        playerData.put(player.getUniqueId(), new PlayerData(player));
    }

    public PlayerData removePlayerData(Player player) {
        return playerData.remove(player.getUniqueId());
    }
}