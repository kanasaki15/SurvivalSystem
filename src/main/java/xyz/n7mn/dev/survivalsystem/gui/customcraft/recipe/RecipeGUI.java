package xyz.n7mn.dev.survivalsystem.gui.customcraft.recipe;

import net.kyori.adventure.text.Component;
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
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIItem;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIListener;
import xyz.n7mn.dev.survivalsystem.gui.customcraft.craft.CraftGUI;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecipeGUI implements GUIListener {

    public void createRecipePreview(Player player, int page) {
        if (1 > page) {
            MessageUtil.sendChat(player, "CANNOT-USE");
        } else {
            RecipeHolder recipeHolder = new RecipeHolder();

            Inventory inventory = Bukkit.createInventory(recipeHolder, 54, Component.text(ChatColor.YELLOW + "| レシピ一覧 - ページ " + page));

            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);

            final ItemStack glass = ItemStackUtil.createItem(Material.GRAY_STAINED_GLASS_PANE, String.valueOf(ChatColor.GRAY));

            HashMap<String, CustomCraftAbstract> data = new HashMap<>(SurvivalInstance.INSTANCE.getCustomCraft().getCraftAbstractHashMap());
            recipeHolder.setCraftRecipe(data);

            int id = 27 * (page - 1);
            final List<CustomCraftAbstract> list = recipeHolder.getCraftRecipe().values().parallelStream()
                    .filter(CustomCraftAbstract::isShow)
                    .toList();

            for (int slot = 0; slot < 54; slot++) {
                if (!previewSlot(slot)) {
                    inventory.setItem(slot, glass);
                } else {
                    if (list.size() > id) {
                        CustomCraftAbstract craftAbstract = list.get(id);

                        ItemStack item = craftAbstract.getItem(null).getItemStack().clone();

                        //lore!
                        if (item.hasLore()) ItemStackUtil.addLore(item, Component.empty(), Component.text(ChatColor.YELLOW + "レシピID: " + craftAbstract.getRecipeID()));
                        else ItemStackUtil.addLore(item, Component.text(ChatColor.YELLOW + "レシピID: " + craftAbstract.getRecipeID()));

                        inventory.setItem(slot, item);

                        recipeHolder.addListener(slot, p -> showRecipe(p, craftAbstract));

                        id++;
                    }
                }
            }

            final int DOWN = page - 1;
            final int UP = page + 1;

            inventory.setItem(45, ItemStackUtil.createItem(Material.ARROW, ChatColor.YELLOW + "← 前のページ (" + DOWN + ")"));
            recipeHolder.addListener(45, p -> createRecipePreview(p, DOWN));

            inventory.setItem(49, ItemStackUtil.createItem(Material.RED_WOOL, ChatColor.RED + "クラフト画面を開く"));
            recipeHolder.addListener(49, p -> p.openInventory(new CraftGUI().createGUI()));

            inventory.setItem(53, ItemStackUtil.createItem(Material.ARROW, ChatColor.YELLOW + "→ 次のページ (" + UP + ")"));
            recipeHolder.addListener(53, p -> createRecipePreview(p, UP));

            player.openInventory(inventory);
        }
    }

    public void showRecipe(Player player, CustomCraftAbstract craftAbstract) {
        final RecipeHolder recipeHolder = new RecipeHolder();

        final Inventory inventory = Bukkit.createInventory(recipeHolder, 54, Component.text(ChatColor.YELLOW + "| " + craftAbstract.getRecipeID() + " のレシピ"));

        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);

        abstractRecipe(inventory);

        if (craftAbstract.isShow()) {

            final List<Integer> allowed = previewCraftList();
            final List<ItemData> recipe = craftAbstract.getUsesItem().getItemData();

            for (int i = 0; i < 9; i++) {

                final int slot = allowed.get(i);
                final ItemData itemData = recipe.get(i);

                inventory.setItem(slot, itemData.getItemStack());
            }

            inventory.setItem(24, craftAbstract.getItem(null).getItemStack());
        }

        inventory.setItem(45, ItemStackUtil.createItem(Material.ARROW, ChatColor.YELLOW + "← 戻る"));
        recipeHolder.addListener(45, p -> createRecipePreview(p, 1));

        player.openInventory(inventory);
    }

    public void abstractRecipe(Inventory inventory) {
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, ItemStackUtil.createItem(Material.GRAY_STAINED_GLASS_PANE, String.valueOf(ChatColor.GRAY)));
        }
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

    public boolean previewSlot(int rawId) {
        return (rawId >= 10 && rawId <= 16) ||
                (rawId >= 19 && rawId <= 25) ||
                (rawId >= 28 && rawId <= 34) ||
                (rawId >= 37 && rawId <= 43);
    }

    /**
     * @return - クラフトスロットを返します
     */
    public List<Integer> previewCraftList() {
        return Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
    }
}
