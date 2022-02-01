package xyz.n7mn.dev.survivalsystem.gui.base;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface GUIListener {

    void onInventoryClickEvent(final InventoryClickEvent e);

    void onInventoryCloseEvent(final InventoryCloseEvent e);

    void onInventoryDragEvent(final InventoryDragEvent e);
}