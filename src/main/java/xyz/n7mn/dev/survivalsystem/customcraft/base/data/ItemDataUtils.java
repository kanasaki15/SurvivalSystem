package xyz.n7mn.dev.survivalsystem.customcraft.base.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

public class ItemDataUtils {
    public static final ItemData AIR_DUMMY = new ItemData(new ItemStack(Material.AIR));

    public static final ItemData INVALID_ITEM = new ItemData(ItemStackUtil.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "不明！ レシピ通りに置いてください"));

    public static final ItemData DIRT_HELMET = new ItemData(ItemStackUtil.createItem(Material.LEATHER_HELMET, ChatColor.DARK_GREEN + "テスト装備"));

    public static ItemData RESISTANCE_RING1 = new ItemData(ItemStackUtil.createItem(Material.GOLD_NUGGET
            , ChatColor.YELLOW + "耐性のリング Ⅰ"
            , new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "resistance_ring")
            , PersistentDataType.INTEGER, 1
            , ChatColor.YELLOW + "耐性のリング"
            , ChatColor.YELLOW + "このアイテムをインベントリーに持つことで"
            , ChatColor.YELLOW + "HPが半分以下になったとき耐性 Ⅰをつける。"));

    public static ItemData RESISTANCE_RING2 = new ItemData(ItemStackUtil.createItem(Material.GOLD_NUGGET
            , ChatColor.YELLOW + "耐性のリング Ⅱ"
            , new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "resistance_ring")
            , PersistentDataType.INTEGER, 2
            , ChatColor.YELLOW + "耐性のリング"
            , ChatColor.YELLOW + "このアイテムをインベントリーに持つことで"
            , ChatColor.YELLOW + "HPが半分以下になったとき耐性 Ⅱをつける。"));

    public static ItemData LAUNCH_PAD = new ItemData(ItemStackUtil.createItem(Material.HEAVY_WEIGHTED_PRESSURE_PLATE
                , ChatColor.YELLOW + "ランチャーパッド"
                , new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "mt_launch_pad")
                , PersistentDataType.INTEGER, 0));


    public static ItemData DIAMOND_PICKAXE = new ItemData(ItemStackUtil.createItem(Material.DIAMOND_PICKAXE, ChatColor.YELLOW + "すごいピッケル", new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "great_pickaxe"), PersistentDataType.INTEGER, 1, ChatColor.YELLOW + "テスト"));

    public static void init() {
        RESISTANCE_RING1.getItemStack().addEnchant(Enchantment.MENDING, 1, true);
        RESISTANCE_RING1.getItemStack().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        RESISTANCE_RING2.getItemStack().addEnchant(Enchantment.MENDING, 1, true);
        RESISTANCE_RING2.getItemStack().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        LAUNCH_PAD.getItemStack().addEnchant(Enchantment.MENDING, 1, true);
        LAUNCH_PAD.getItemStack().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }
}