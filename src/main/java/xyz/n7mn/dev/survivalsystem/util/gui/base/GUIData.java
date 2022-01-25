package xyz.n7mn.dev.survivalsystem.util.gui.base;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GUIData {
    private final UUID UUID;
    private final String name;

    public GUIData (UUID uuid, String name) {
        this.UUID = uuid;
        this.name = name;
    }
}
