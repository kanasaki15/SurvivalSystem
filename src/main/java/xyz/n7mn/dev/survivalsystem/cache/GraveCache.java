package xyz.n7mn.dev.survivalsystem.cache;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.data.GraveInventoryData;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class GraveCache {
    public static Map<UUID, GraveInventoryData> graveCache = new HashMap<>();

    public void handle() {
        for (GraveInventoryData data : graveCache.values()) {
            Bukkit.getScheduler().runTask(SurvivalInstance.INSTANCE.getPlugin(), () -> {

                if (data.getWorld() == null || !data.isActive()) {

                    data.remove(data.isActive());

                    return;
                }

                Entity entity = data.getWorld().getEntity(data.getArmorStandUUID());

                if (entity != null) {

                    final int time = entity.getPersistentDataContainer().get(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "delete_time"), PersistentDataType.INTEGER) - 1;

                    if (time > 0) {
                        entity.setCustomName(MessageUtil.replaceFromConfig("GRAVE-NAME", "%name%|" + data.getPlayerName(), "%time%|" + time));

                        entity.getPersistentDataContainer().set(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "delete_time"), PersistentDataType.INTEGER, time);
                    } else {
                        data.translateSerializable().forEach(itemStack -> ((Item) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.DROPPED_ITEM)).setItemStack(itemStack));
                        entity.remove();

                        data.remove();
                    }
                } else {
                    data.remove(true);

                    if (SurvivalInstance.INSTANCE.getPlugin().getConfig().getBoolean("GraveCacheWarning"))
                        Bukkit.getLogger().warning("[お墓] キャッシュにいらないデータが入っていました");
                }
            });
        }
    }

    public void refundItem(Player player, GraveInventoryData data) {
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);

        data.translateSerializable().forEach(itemStack -> {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(itemStack);
            } else {
                MessageUtil.sendChat(player, "GRAVE-DROP-ITEM", "%item%|" + WordUtils.capitalizeFully(itemStack.getType().name().toLowerCase().replaceAll("_", " ")));
                ((Item) player.getWorld().spawnEntity(player.getLocation(), EntityType.DROPPED_ITEM)).setItemStack(itemStack);
            }
        });
    }

    public void reEntry() {
        graveCache.clear();

        SurvivalInstance.INSTANCE.getConnection().getGraveTable().get(result -> graveCache.putAll(result));
    }

    public void init() {
        SurvivalInstance.INSTANCE.getConnection().getGraveTable().get(result -> graveCache.putAll(result));
    }
}