package xyz.n7mn.dev.survivalsystem.gui.customcraft;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.advancement.data.CustomCraftCreateAdvancement;
import xyz.n7mn.dev.survivalsystem.customcraft.base.CustomCraftAbstract;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemData;
import xyz.n7mn.dev.survivalsystem.customcraft.base.data.ItemDataUtils;
import xyz.n7mn.dev.survivalsystem.gui.base.GUIItem;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

import java.util.Arrays;
import java.util.List;

public class CraftGUI implements Listener {


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
        inventory.setItem(24, ItemDataUtils.INVALID_ITEM.getItemStack());
    }

    public void checkUpdates(CraftHolder craftHolder) {
        for (CustomCraftAbstract data : SurvivalInstance.INSTANCE.getCustomCraft().getCraftAbstractHashMap().values()) {
            if (data.create().equals(craftHolder.translateCustomCraftData(), true)) {
                craftHolder.getInventory().setItem(24, data.getItem().getItemStack());
                return;
            }
        }
        craftHolder.getInventory().setItem(24, ItemDataUtils.INVALID_ITEM.getItemStack());
    }

    public boolean craft(CraftHolder craftHolder, ItemStack cursor, HumanEntity player, InventoryAction inventoryAction) {
        if (cursor != null && cursor.getType() == Material.AIR) {
            if (inventoryAction != InventoryAction.HOTBAR_MOVE_AND_READD) {
                for (CustomCraftAbstract data : SurvivalInstance.INSTANCE.getCustomCraft().getCraftAbstractHashMap().values()) {
                    if (data.create().equals(craftHolder.translateCustomCraftData(), true)) {
                        if (inventoryAction == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                            while (player.getInventory().firstEmpty() != -1) {
                                ItemData itemData = getItemChecksData(craftHolder);
                                if (itemData == null) break;

                                player.getInventory().addItem(itemData.getItemStack());
                            }
                            checkUpdates(craftHolder);
                        } else {
                            push(craftHolder, data);
                        }

                        ((Player) player).getAdvancementProgress(Bukkit.getAdvancement(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), CustomCraftCreateAdvancement.ID))).awardCriteria("grant");

                        return inventoryAction != InventoryAction.MOVE_TO_OTHER_INVENTORY;
                    }
                }
            } else {
                player.sendMessage(Component.text(ChatColor.RED + "[!] それはできませんよ"));
            }
        }
        return false;
    }

    public ItemData getItemChecksData(CraftHolder craftHolder) {
        for (CustomCraftAbstract data : SurvivalInstance.INSTANCE.getCustomCraft().getCraftAbstractHashMap().values()) {
            if (data.create().equals(craftHolder.translateCustomCraftData(), true)) {
                return getItem(craftHolder, data);
            }
        }
        return null;
    }

    public ItemData getItem(CraftHolder craftHolder, CustomCraftAbstract data) {
        List<Integer> deny = denyList();

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = craftHolder.getInventory().getItem(deny.get(i));

            if (itemStack != null && itemStack.getType() != Material.AIR) {
                itemStack.setAmount(itemStack.getAmount() - data.getUsesItem().getItemData().get(i).getItemStack().getAmount());
            }
        }

        return data.getItem();
    }


    public void push(CraftHolder craftHolder, CustomCraftAbstract data) {
        getItem(craftHolder, data);
    }

    @EventHandler
    public void onInventoryClickEvent(final InventoryClickEvent e) {
        if (e.getClickedInventory() == e.getView().getTopInventory() && e.getView().getTopInventory().getHolder() instanceof CraftHolder craftHolder) {
            if (craftHolder.getCommandID() == 1) {
                if (!deny(e.getRawSlot()) && e.getRawSlot() != 24) e.setCancelled(true);
                else craftHolder.setInventory(e.getClickedInventory());

                GUIItem guiItem = craftHolder.getHashMap().get(e.getRawSlot());
                if (guiItem != null) {
                    guiItem.execute((Player) e.getWhoClicked());
                }

                if (e.getRawSlot() == 24) {
                    if (!craft(craftHolder, e.getCursor(), e.getWhoClicked(), e.getAction())) e.setCancelled(true);
                    else Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> checkUpdates(craftHolder));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDragEvent(final InventoryDragEvent e) {
        if (e.getWhoClicked().getOpenInventory().getTopInventory() == e.getInventory() && e.getView().getTopInventory().getHolder() instanceof CraftHolder craftHolder) {
            if (craftHolder.getCommandID() == 1) {
                for (int slot : e.getRawSlots()) {
                    if (deny(slot)) craftHolder.setInventory(e.getView().getTopInventory());

                    GUIItem guiItem = craftHolder.getHashMap().get(slot);
                    if (guiItem != null) {
                        guiItem.execute((Player) e.getWhoClicked());

                        break;
                    }

                    if (slot == 24) {
                        if (!craft(craftHolder, e.getCursor(), e.getWhoClicked(), null)) e.setCancelled(true);
                        else checkUpdates(craftHolder);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof CraftHolder craftHolder) {
            if (craftHolder.getCommandID() == 1) {
                for (int refund : denyList()) {
                    ItemStack itemStack = e.getInventory().getItem(refund);

                    if (itemStack != null && itemStack.getType() != Material.AIR) ItemStackUtil.addItem(e.getPlayer(), itemStack);
                }
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