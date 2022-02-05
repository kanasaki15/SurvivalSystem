package xyz.n7mn.dev.survivalsystem.customcraft.base.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

public class ItemDataUtils {
    public static final ItemData AIR_DUMMY = new ItemData(new ItemStack(Material.AIR));

    public static final ItemData INVALID_ITEM = new ItemData(ItemStackUtil.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "不明！ レシピ通りに置いてください"));

    public static final ItemData DIRT_HELMET = new ItemData(ItemStackUtil.createItem(Material.LEATHER_HELMET, ChatColor.DARK_GREEN + "テスト装備"));
}
