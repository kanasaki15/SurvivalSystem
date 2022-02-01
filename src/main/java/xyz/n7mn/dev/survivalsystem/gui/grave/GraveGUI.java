package xyz.n7mn.dev.survivalsystem.gui.grave;

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
import xyz.n7mn.dev.survivalsystem.cache.GraveCache;
import xyz.n7mn.dev.survivalsystem.data.GraveInventoryData;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIListener;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIData;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIItem;

import java.util.Objects;

/**
 * https://www.spigotmc.org/threads/custom-inventoryholders.253149/
 * https://www.spigotmc.org/threads/inventory-system-tutorial-gui.258035/
 */
public class GraveGUI implements Listener, GUIListener {

    public int MAX_ALLOWED = 45;

    public Inventory createPage(GUIData guiData, int page) {
        GraveHolder guiHolder = new GraveHolder(1);

        guiHolder.setGuiData(guiData);

        Inventory inventory = Bukkit.createInventory(guiHolder, 54, Component.text(ChatColor.YELLOW + "お墓" + guiData.getName() + "の履歴"));

        SurvivalInstance.INSTANCE.getConnection().getGraveTable().getAll(guiData.getUUID(), list -> {
            list.sort(((o1, o2) -> Long.compare(o2.getTimestamp().getTime(), o1.getTimestamp().getTime())));

            guiHolder.setData(list);

            generatePage(inventory, page);
        });

        return inventory;
    }

    public void nextPage(Player player, GraveHolder guiHolder, int page) {
        if (1 > page) {
            MessageUtil.sendChat(player, "CANNOT-USE");
        } else {
            GraveHolder holder = new GraveHolder(1);

            holder.setGuiData(guiHolder.getGuiData());
            holder.setData(guiHolder.getData());
            holder.setBasePlayer(player);

            Inventory inventory = Bukkit.createInventory(holder, 54, Component.text(ChatColor.YELLOW + "お墓" + holder.getTargetPlayer().getName() + "の履歴"));

            generatePage(inventory, page);

            player.openInventory(inventory);
        }
    }

    public void generatePage(Inventory inventory, int page) {
        int MIN = MAX_ALLOWED * (page - 1);

        GraveHolder guiHolder = (GraveHolder) Objects.requireNonNull(inventory.getHolder());

        for (int i = MIN; i < MAX_ALLOWED * page; i++) {
            try {
                GraveInventoryData data = guiHolder.getData().get(i);

                inventory.addItem(ItemStackUtil.createItem(Material.CHEST, ChatColor.GREEN + "お墓 #" + (i + 1),
                        ChatColor.YELLOW + "時間: " + data.getTimestamp(),
                        ChatColor.YELLOW + "ワールド: " + data.getWorld().getName(),
                        ChatColor.YELLOW + "ユーザー名: " + data.getPlayerName(),
                        ChatColor.YELLOW + "UUID: " + data.getUUID(),
                        ChatColor.YELLOW + "存在してる: " + data.isActive(),
                        ChatColor.YELLOW + "アーマースタンド: " + data.getArmorStandUUID()));

                guiHolder.addListener(i - MIN, p -> createGravePreview(p, guiHolder, data));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        for (int i = 46; i < 53; i++) {
            inventory.setItem(i, ItemStackUtil.createItem(Material.RED_STAINED_GLASS_PANE, String.valueOf(ChatColor.RED)));
        }

        int DOWN = page - 1;
        int UP = page + 1;

        inventory.setItem(45, ItemStackUtil.createItem(Material.ARROW, ChatColor.YELLOW + "← 前のページ (" + DOWN + ")"));
        guiHolder.addListener(45, p -> nextPage(p, guiHolder, DOWN));

        inventory.setItem(53, ItemStackUtil.createItem(Material.ARROW, ChatColor.YELLOW + "→ 次のページ (" + UP + ")"));
        guiHolder.addListener(53, p -> nextPage(p, guiHolder, UP));
    }

    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent e) {
        if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof GraveHolder guiHolder) {
            e.setCancelled(true);

            GUIItem guiItem = guiHolder.getHashMap().get(e.getRawSlot());
            if (guiItem != null) {
                guiItem.execute((Player) e.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void onInventoryDragEvent(final InventoryDragEvent e) {
        if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof GraveHolder) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onInventoryCloseEvent(final InventoryCloseEvent e) {

    }

    public void createGravePreview(Player player, GraveHolder guiHolder, GraveInventoryData data) {
        GraveHolder holder = new GraveHolder(2);

        Inventory inventory = Bukkit.createInventory(holder, 54, Component.text(ChatColor.YELLOW + "お墓" + player.getName() + "の履歴"));
        for (ItemStack itemStack : data.translateSerializable()) {
            inventory.addItem(itemStack);
        }

        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, ItemStackUtil.createItem(Material.RED_STAINED_GLASS_PANE, String.valueOf(ChatColor.RED)));
        }

        inventory.setItem(45, ItemStackUtil.createItem(Material.RED_WOOL, ChatColor.RED + "← 戻る"));
        holder.addListener(45, p -> p.openInventory(createPage(guiHolder.getGuiData(), 1)));

        inventory.setItem(53, ItemStackUtil.createItem(Material.GREEN_WOOL, ChatColor.GREEN + "→ アイテムを取得する"));
        holder.addListener(53, p -> GraveCache.refundItem(p, data));

        player.openInventory(inventory);
    }
}