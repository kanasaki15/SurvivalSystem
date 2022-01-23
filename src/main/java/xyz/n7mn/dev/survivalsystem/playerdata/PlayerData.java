package xyz.n7mn.dev.survivalsystem.playerdata;

import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.n7mn.dev.survivalsystem.playerdata.impl.VanishData;
import xyz.n7mn.dev.survivalsystem.util.VanishManager;

@Getter
public class PlayerData {

    private final Player player;
    private final VanishData vanishData = new VanishData(this);

    public PlayerData(Player player) {
        this.player = player;
    }
}
