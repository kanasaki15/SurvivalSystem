package xyz.n7mn.dev.survivalsystem.gui;

import org.bukkit.Bukkit;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIEvent;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIListener;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    public void init() {
        Bukkit.getPluginManager().registerEvents(new GUIEvent(), SurvivalInstance.INSTANCE.getPlugin());
    }

    private final List<GUIListener> listener = new ArrayList<>();

    public void addListener(GUIListener listener) {
        this.listener.add(listener);
    }

    public List<GUIListener> getAll() {
        return listener;
    }
}