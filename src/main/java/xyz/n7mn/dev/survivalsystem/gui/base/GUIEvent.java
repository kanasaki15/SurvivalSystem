package xyz.n7mn.dev.survivalsystem.gui.base;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

import java.util.ArrayList;
import java.util.List;

public class GUIEvent implements Listener {

    public void onInventoryClickEvent(final InventoryClickEvent e) {
        SurvivalInstance.INSTANCE.getGuiManager().getAll().forEach(event -> event.onInventoryClickEvent(e));
    }

    @EventHandler
    public void onInventoryDragEvent(final InventoryDragEvent e) {
        SurvivalInstance.INSTANCE.getGuiManager().getAll().forEach(event -> event.onInventoryDragEvent(e));
    }

    @EventHandler
    public void onInventoryCloseEvent(final InventoryCloseEvent e) {
        SurvivalInstance.INSTANCE.getGuiManager().getAll().forEach(event -> event.onInventoryCloseEvent(e));
    }
}
