package xyz.n7mn.dev.survivalsystem.event;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.cache.GraveCache;
import xyz.n7mn.dev.survivalsystem.cache.serializable.ItemStackSerializable;
import xyz.n7mn.dev.survivalsystem.data.GraveInventoryData;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;
import xyz.n7mn.dev.survivalsystem.sql.table.GraveTable;
import xyz.n7mn.dev.survivalsystem.util.*;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EventListener implements Listener {

    @EventHandler
    public void AsyncChatEvent(AsyncChatEvent e) {
        Component message = e.message();

        e.setCancelled(true);
    }


    @EventHandler
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {

        // op以外には見せたくないものとか
        if (!e.getPlayer().isOp()) {
            if (e.getMessage().startsWith("/help")) {
                e.setMessage("/rule");
            }

            if (e.getMessage().equals("/pl") || e.getMessage().startsWith("/plugins")) {
                e.getPlayer().sendMessage("Plugins (0):");
                e.setCancelled(true);
            }

            if (e.getMessage().equals("/stop")) {
                e.setCancelled(true);
            }

            if (e.getMessage().startsWith("/version") || e.getMessage().startsWith("/ver ") || e.getMessage().equals("/ver")) {

                Plugin plugin = SurvivalInstance.INSTANCE.getPlugin();

                TextComponent text1 = Component.text("" +
                        "ななみ生活鯖\n接続可能バージョン : 1.16 - " + plugin.getServer().getMinecraftVersion() + "\n" +
                        "This server is running NanamiSurvivalServer\nConnectable Versions : 1.16.4 - " + plugin.getServer().getMinecraftVersion() + "\n\n" +
                        "不具合報告はDiscordの「#せいかつ鯖-不具合報告」へどうぞ。\n" +
                        "If you want to report a bug, please go to \"#せいかつ鯖-不具合報告\" on Discord.\n"
                );
                TextComponent text2 = Component.text(ChatColor.BLUE + "[Discord]").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, plugin.getConfig().getString("DiscordURL")));
                text1.append(text2);

                e.getPlayer().sendMessage(text1);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        if (!VanishManager.hasData(e.getPlayer())) {
            MessageUtil.sendMessageBroadCast("QUIT-MESSAGE", "%player%|" + e.getPlayer().getName());
        }

        e.quitMessage(Component.empty());
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        PlayerDataUtil.putPlayerData(e.getPlayer());
        //--- get PlayerData
        final boolean success = VanishManager.handleHide(e.getPlayer());

        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        data.getVanishData().setVanished(!success);

        if (success) {
            MessageUtil.sendMessageBroadCast("JOIN-MESSAGE", "%player%|" + e.getPlayer().getName());
        }

        e.joinMessage(Component.empty());
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld().getEnvironment() != World.Environment.THE_END) {

            GraveTable deathTable = SurvivalInstance.INSTANCE.getConnection().getGraveTable();

            ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM);
            armorStand.setInvisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setSmall(true);
            armorStand.setAI(false);
            armorStand.setCustomNameVisible(true);
            armorStand.addDisabledSlots(EquipmentSlot.HEAD); //4096
            armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

            armorStand.getEquipment().setHelmet(new ItemStack(Material.CHEST, 64));

            final int time = SurvivalInstance.INSTANCE.getPlugin().getConfig().getInt("GraveTime");

            armorStand.setCustomName(MessageUtil.replaceFromConfig("GRAVE-NAME", "%name%|" + e.getPlayer().getName(), "%time%|" + time));
            armorStand.getPersistentDataContainer().set(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(),"delete_time"), PersistentDataType.INTEGER, time);

            List<Map<String, Object>> list = new ArrayList<>();
            e.getDrops().forEach(i -> list.add(i.serialize()));

            GraveInventoryData data = new GraveInventoryData(Timestamp.valueOf(LocalDateTime.now()), player.getWorld().getName(), player.getName(), e.getPlayer().getUniqueId(), new ItemStackSerializable(list), armorStand.getUniqueId());
            deathTable.put(data);
            GraveCache.graveCache.put(armorStand.getUniqueId(), data);
        } else {
            e.setKeepInventory(true);
        }

        e.setKeepLevel(true);
        e.setShouldDropExperience(false);
        e.getDrops().clear();
    }

    @EventHandler
    public void onEntityInteractEvent(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            GraveInventoryData data = GraveCache.graveCache.get(e.getRightClicked().getUniqueId());
            if (data != null) {
                e.setCancelled(true);

                e.getPlayer().sendMessage(MessageManager.getString("CANNOT-USE"));
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            GraveInventoryData data = GraveCache.graveCache.get(e.getRightClicked().getUniqueId());

            //equals使わないと動かない気がする
            if (data != null && data.getUUID().equals(e.getPlayer().getUniqueId())) {
                e.getRightClicked().remove();

                GraveCache.refundItem(e.getPlayer(), data);

                data.remove();
            }
        }
    }

    @EventHandler
    public void onEntityAddToWorldEvent(EntityAddToWorldEvent e) {
        if (e.getEntity().getType() == EntityType.ARMOR_STAND) {
            SurvivalInstance.INSTANCE.getConnection().getGraveTable().get(e.getEntity().getUniqueId(), data -> {
                if (data != null && GraveCache.graveCache.get(data.getArmorStandUUID()) == null) {
                    GraveCache.graveCache.put(data.getArmorStandUUID(), data);
                }
            });
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.BEE_NEST || e.getClickedBlock().getType() == Material.BEEHIVE)) {
            if ((e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GLASS_BOTTLE) || (e.getHand() == EquipmentSlot.OFF_HAND && e.getPlayer().getInventory().getItemInOffHand().getType() == Material.GLASS_BOTTLE)) {

                final int type = Integer.parseInt(e.getClickedBlock().getBlockData().getAsString().replaceAll("[^0-9]", ""));

                if (type == 5) {
                    final int chance = MessageManager.getInt("HONEY-RARE-ITEM-CHANCE");

                    if (new SecureRandom().nextInt(100) < chance) {
                        ItemStack itemStack = ItemStackUtil.createItem(Material.HONEY_BOTTLE, MessageUtil.replaceFromConfig("HONEY-ITEM-NAME"), MessageUtil.replaceList("HONEY-ITEM-LORE", "%grade%|" + "Ⅰ"));

                        itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        itemStack.addEnchant(Enchantment.MENDING, 1, true);

                        ItemMeta itemMeta = itemStack.getItemMeta();

                        itemMeta.getPersistentDataContainer().set(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "gq_honey"), PersistentDataType.INTEGER, 1);

                        itemStack.setItemMeta(itemMeta);

                        MessageUtil.sendChat(e.getPlayer(), "HONEY-RARE-ITEM", "%chance%|" + chance);

                        ItemStackUtil.addItem(e.getPlayer(), itemStack);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityPortalEvent(EntityPortalEvent e) {
        //通常テレポートさせてはいけません！
        if (GraveCache.graveCache.get(e.getEntity().getUniqueId()) != null)
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.HONEY_BOTTLE) {

            NamespacedKey namespacedKey = new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "gq_honey");

            if (e.getItem().getPersistentDataContainer().has(namespacedKey)) {
                switch (e.getItem().getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER)) {
                    case 1: {
                        e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + 3);

                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 20 * 300, 1));
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 300, 1));
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60, 1));
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 60, 1));
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 1));
                    }
                }

                e.getPlayer().getAdvancementProgress(Bukkit.getAdvancement(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "great_honey"))).awardCriteria("grant");
            }
        }
    }

    @EventHandler
    public void advancement(PlayerAdvancementDoneEvent e) {
        SurvivalInstance.INSTANCE.getAdvancement().getRewardManager().execute(e.getPlayer(), e.getAdvancement().getKey().getKey());
    }
}