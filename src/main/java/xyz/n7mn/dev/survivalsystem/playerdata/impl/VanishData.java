package xyz.n7mn.dev.survivalsystem.playerdata.impl;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;

@Getter @Setter
public class VanishData {

    private PlayerData data;

    public VanishData(PlayerData data) {
        this.data = data;
    }

    private boolean isVanished = false;

    public void handle() {
        if (data.getVanishData().isVanished()) {
            data.getPlayer().sendActionBar(Component.text(MessageUtil.replaceString("VANISH-ACTIONBAR")));
        }
    }
}