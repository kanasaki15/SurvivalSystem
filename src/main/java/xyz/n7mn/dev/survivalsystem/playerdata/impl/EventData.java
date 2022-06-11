package xyz.n7mn.dev.survivalsystem.playerdata.impl;

import lombok.Getter;
import lombok.Setter;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;

@Getter @Setter
public class EventData {

    private PlayerData data;

    public EventData(PlayerData data) {
        this.data = data;
    }

    private long lastBuildersWand;
}
