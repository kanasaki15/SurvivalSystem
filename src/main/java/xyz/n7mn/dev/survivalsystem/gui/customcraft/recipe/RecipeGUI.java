package xyz.n7mn.dev.survivalsystem.gui.customcraft.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIListener;

public class RecipeGUI implements GUIListener {
    public void previewCraftRecipe(Player player) {
        Inventory inventory = Bukkit.createInventory(new RecipeHolder(), 54);

        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);

        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, itemStack);
        }

        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, itemStack);
        }

        setItem(inventory, itemStack, 9, 17, 18, 26, 27, 35, 36, 44, 45, 53);

        player.openInventory(inventory);
    }

    public void setItem(Inventory inventory, ItemStack itemStack, int... range) {
        for (int rawId : range) {
            inventory.setItem(rawId, itemStack);
        }
    }

    @Override
    public void onInventoryClickEvent(final InventoryClickEvent e) {

    }

    @Override
    public void onInventoryDragEvent(final InventoryDragEvent e) {

    }

    @Override
    public void onInventoryCloseEvent(final InventoryCloseEvent e) {

    }
}
