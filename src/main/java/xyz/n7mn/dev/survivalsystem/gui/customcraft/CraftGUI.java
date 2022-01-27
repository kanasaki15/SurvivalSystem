package xyz.n7mn.dev.survivalsystem.gui.customcraft;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIItem;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

import java.util.Arrays;
import java.util.List;

public class CraftGUI implements Listener {
    private final ItemStack invalid = ItemStackUtil.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "不明！ レシピ通りに置いてください");

    public Inventory createGUI() {
        CraftHolder craftHolder = new CraftHolder(1);

        Inventory inventory = Bukkit.createInventory(craftHolder, 54, Component.text("カスタム作業台"));

        createCraftGUI(craftHolder, inventory);

        craftHolder.setInventory(inventory);

        return inventory;
    }

    public void createCraftGUI(CraftHolder craftHolder, Inventory inventory) {
        for (int i = 0; i < 54; i++) {
            if (!deny(i)) {
                inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            } else {
                craftHolder.addListener(i, player -> Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> checkUpdates(craftHolder)));
            }
        }
        inventory.setItem(24, invalid);
    }

    public void checkUpdates(CraftHolder craftHolder) {
        for (CustomCraftAbstract data : SurvivalInstance.INSTANCE.getCustomCraft().getCraftAbstractHashMap().values()) {
            if (data.create().equals(craftHolder.translateCustomCraftData())) {
                Bukkit.broadcastMessage("a");
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent e) {
        if (e.getClickedInventory() == e.getView().getTopInventory() && e.getView().getTopInventory().getHolder() instanceof CraftHolder craftHolder) {
            if (!deny(e.getRawSlot())) e.setCancelled(true);
            else craftHolder.setInventory(e.getClickedInventory());

            GUIItem guiItem = craftHolder.getHashMap().get(e.getRawSlot());
            if (guiItem != null) {
                guiItem.execute((Player) e.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void onInventoryDragEvent(final InventoryDragEvent e) {
        if (e.getWhoClicked().getOpenInventory().getTopInventory() == e.getInventory() && e.getView().getTopInventory().getHolder() instanceof CraftHolder craftHolder) {
            for (int slot : e.getRawSlots()) {
                if (deny(slot)) craftHolder.setInventory(e.getView().getTopInventory());

                GUIItem guiItem = craftHolder.getHashMap().get(slot);
                if (guiItem != null) {
                    guiItem.execute((Player) e.getWhoClicked());

                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof CraftHolder craftHolder && craftHolder.getCommandID() == 1) {
            for (int refund : denyList()) {
                ItemStack itemStack = e.getInventory().getItem(refund);

                if (itemStack != null && itemStack.getType() != Material.AIR) ItemStackUtil.addItem(e.getPlayer(), itemStack);
            }
        }
    }

    public boolean deny(final int rawSlot) {
        return (rawSlot >= 10 && rawSlot <= 12) ||
                (rawSlot >= 19 && rawSlot <= 21) ||
                (rawSlot >= 28 && rawSlot <= 30);
    }

    public List<Integer> denyList() {
        return Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
    }
}