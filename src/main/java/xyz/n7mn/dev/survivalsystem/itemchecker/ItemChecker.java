package xyz.n7mn.dev.survivalsystem.itemchecker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.advancement.base.check.ItemCheck;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIListener;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class ItemChecker implements GUIListener, Listener {
    private final List<ItemCheck> itemChecks = new ArrayList<>();

    public void init() {
        getServer().getPluginManager().registerEvents(this, SurvivalInstance.INSTANCE.getPlugin());
        SurvivalInstance.INSTANCE.getGuiManager().addListener(this);
    }

    public void register(ItemCheck itemCheck) {
        itemChecks.add(itemCheck);
    }

    public void forEach(Player player, ItemStack itemStack) {
        itemChecks.forEach(itemCheck -> itemCheck.item(player, itemStack));
    }


    @Override
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (!e.isCancelled() && e.getWhoClicked() instanceof Player player) {
            forEach(player, e.getCurrentItem());
        }
    }

    @Override
    public void onInventoryCloseEvent(InventoryCloseEvent e) {

    }

    @EventHandler
    public void onEntityPickupItemEvent(EntityPickupItemEvent e) {
        if (!e.isCancelled() && e.getEntity() instanceof Player player) {
            forEach(player, e.getItem().getItemStack());
        }
    }

    @Override
    public void onInventoryDragEvent(InventoryDragEvent e) {
        if (!e.isCancelled() && e.getWhoClicked() instanceof Player player) {
            e.getNewItems().values().forEach(item -> forEach(player, item));
        }
    }
}