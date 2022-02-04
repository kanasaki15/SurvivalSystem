package xyz.n7mn.dev.survivalsystem.gui.customcraft.recipe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIItem;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIListener;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

public class RecipeGUI implements GUIListener {
    public void previewCraftRecipe(Player player) {
        Inventory inventory = Bukkit.createInventory(new RecipeHolder(), 54);

        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);

        ItemStack itemStack = ItemStackUtil.createItem(Material.GRAY_STAINED_GLASS_PANE, String.valueOf(ChatColor.GRAY));

        for (int i = 0; i < 54; i++) {
            if (!recipeSlot(i)) inventory.setItem(i, itemStack);
        }

        player.openInventory(inventory);
    }

    @Override
    public void onInventoryClickEvent(final InventoryClickEvent e) {
        if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof RecipeHolder recipeHolder) {
            e.setCancelled(true);

            GUIItem guiItem = recipeHolder.getHashMap().get(e.getRawSlot());
            if (guiItem != null) {
                guiItem.execute((Player) e.getWhoClicked());
            }
        }
    }

    @Override
    public void onInventoryDragEvent(final InventoryDragEvent e) {
        if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof RecipeHolder recipeHolder) {
            e.setCancelled(true);

            for (int slot : e.getRawSlots()) {
                GUIItem guiItem = recipeHolder.getHashMap().get(slot);
                if (guiItem != null) {
                    guiItem.execute((Player) e.getWhoClicked());
                }
            }
        }
    }

    @Override
    public void onInventoryCloseEvent(final InventoryCloseEvent e) {

    }

    public boolean recipeSlot(int rawId) {
        return (rawId >= 10 && rawId <= 16) ||
                (rawId >= 19 && rawId <= 25) ||
                (rawId >= 28 && rawId <= 34) ||
                (rawId >= 37 && rawId <= 43);
    }
}
