package xyz.n7mn.dev.survivalsystem.gui;

import org.bukkit.Bukkit;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIEvent;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIListener;
import xyz.n7mn.dev.survivalsystem.gui.customcraft.craft.CraftGUI;
import xyz.n7mn.dev.survivalsystem.gui.customcraft.recipe.RecipeGUI;
import xyz.n7mn.dev.survivalsystem.gui.grave.GraveGUI;
import xyz.n7mn.dev.survivalsystem.itemchecker.ItemChecker;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    public void init() {
        Bukkit.getPluginManager().registerEvents(new GUIEvent(), SurvivalInstance.INSTANCE.getPlugin());

        listener.add(new GraveGUI());
        listener.add(new CraftGUI());
        listener.add(new RecipeGUI());
        listener.add(new ItemChecker());
    }

    private final List<GUIListener> listener = new ArrayList<>();

    public void addListener(GUIListener listener) {
        this.listener.add(listener);
    }

    public List<GUIListener> getAll() {
        return listener;
    }
}