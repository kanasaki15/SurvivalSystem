package xyz.n7mn.dev.survivalsystem.playerdata.impl;

import lombok.Getter;
import lombok.Setter;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;

@Getter @Setter
public class VanishManager {

    private PlayerData data;

    public VanishManager(PlayerData data) {
        this.data = data;
    }

    private boolean isVanished = false;
}