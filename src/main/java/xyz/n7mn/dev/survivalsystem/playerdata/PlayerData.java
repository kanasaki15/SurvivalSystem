package xyz.n7mn.dev.survivalsystem.playerdata;

import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.n7mn.dev.survivalsystem.playerdata.impl.VanishManager;
import xyz.n7mn.dev.survivalsystem.util.PlayerDataUtil;

@Getter
public class PlayerData {

    private final Player player;
    private final VanishManager vanishManager = new VanishManager(this);

    public PlayerData(Player player) {
        this.player = player;
    }
}
