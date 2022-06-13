package xyz.n7mn.dev.survivalsystem.customcraft.item;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.itemchecker.base.TickCheck;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;
import xyz.n7mn.dev.survivalsystem.util.ParticleUtils;
import xyz.n7mn.dev.survivalsystem.util.PlayerDataUtil;
import xyz.n7mn.dev.survivalsystem.util.pair.Pair;

import java.util.List;

public class BuildersWand implements TickCheck, Listener {

    public BuildersWand() {
        Bukkit.getPluginManager().registerEvents(this, SurvivalInstance.INSTANCE.getPlugin());
    }

    @Override
    public void onTick(Player player) {
        if (player.getEquipment().getItemInMainHand().getType() == Material.STICK
            && player.getEquipment().getItemInMainHand().getPersistentDataContainer().has(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "builders_wand"), PersistentDataType.INTEGER)) {

            List<Block> blocks = player.getLastTwoTargetBlocks(null, 4);

            int totals = getTotal(blocks);

            if (totals == 1 && blocks.size() == 2) {
                Block block = blocks.get(0);

                final int length = player.getEquipment().getItemInMainHand().getPersistentDataContainer()
                        .get(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "builders_wand"), PersistentDataType.INTEGER);

                Vector vec = player.getLocation().getDirection();
                final double dirX = Math.abs(vec.getX());
                final double dirY = Math.abs(vec.getY());
                final double dirZ = Math.abs(vec.getZ());

                if (dirX > dirZ && dirX > dirY) {
                    run(block, 0, length, box -> ParticleUtils.summonOutlineParticle(player, player.getWorld(), box, Particle.SOUL_FIRE_FLAME));
                } else if (dirY > dirZ) {
                    run(block, 1, length, box -> ParticleUtils.summonOutlineParticle(player, player.getWorld(), box, Particle.SOUL_FIRE_FLAME));
                } else {
                    run(block, 2, length, box -> ParticleUtils.summonOutlineParticle(player, player.getWorld(), box, Particle.SOUL_FIRE_FLAME));
                }

                final Pair<Material, Integer> pair = calculate(player);

                player.sendActionBar(Component.text(MessageUtil.replaceFromConfig("BUILDERS-WAND-ACTIONBAR", "%type%|" + WordUtils.capitalizeFully(pair.getKey().name().toLowerCase().replaceAll("_", " ")), "%amount%|" + pair.getValue())));
            }
        }
    }

    public Pair<Material, Integer> calculate(Player player) {
        Pair<Material, Integer> pair = new Pair<>(Material.AIR, 0);

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null
                    && item.getType().isBlock()
                    && item.getType().isSolid()) {

                if (pair.getKey() == Material.AIR) {
                    pair.setKey(item.getType());
                }

                if (item.getType() == pair.getKey()) {
                    pair.setValue(pair.getValue() + item.getAmount());
                }
            }
        }

        return pair;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (e.getClickedBlock() != null
                && e.getAction() == Action.RIGHT_CLICK_BLOCK
                && e.getHand() == EquipmentSlot.HAND
                && System.currentTimeMillis() - data.getEventData().getLastBuildersWand() > 50
                && e.getItem() != null
                && e.getItem().getPersistentDataContainer().has(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "builders_wand"), PersistentDataType.INTEGER)) {

            List<Block> blocks = e.getPlayer().getLastTwoTargetBlocks(null, 4);

            final int totals = getTotal(blocks);

            if (totals == 1 && blocks.size() == 2) {
                Block block = blocks.get(0);

                final int length = e.getItem().getPersistentDataContainer()
                        .get(new NamespacedKey(SurvivalInstance.INSTANCE.getPlugin(), "builders_wand"), PersistentDataType.INTEGER);

                Vector vec = e.getPlayer().getLocation().getDirection();
                final double dirX = Math.abs(vec.getX());
                final double dirY = Math.abs(vec.getY());
                final double dirZ = Math.abs(vec.getZ());

                if (dirX > dirZ && dirX > dirY) {
                    run(block, 0, length, box -> place(data.getPlayer(), box.getMin().toLocation(e.getClickedBlock().getWorld())));
                } else if (dirY > dirZ) {
                    run(block, 1, length, box -> place(data.getPlayer(), box.getMin().toLocation(e.getClickedBlock().getWorld())));
                } else {
                    run(block, 2, length, box -> place(data.getPlayer(), box.getMin().toLocation(e.getClickedBlock().getWorld())));
                }

                data.getEventData().setLastBuildersWand(System.currentTimeMillis());
            }
        }
    }

    public void place(Player player, Location location) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null
                    && item.getType().isBlock()
                    && item.getType().isSolid()
                    && !item.hasItemMeta()
                    && !item.getType().name().contains("DOOR")
                    && !item.getType().name().contains("BED")
                    && !item.getType().name().contains("SHULKER")) {

                item.setAmount(item.getAmount() - 1);

                if (item.getAmount() == 0) {
                    item.setType(Material.AIR);
                }

                Block block = location.getBlock();

                block.setType(item.getType());
                block.getState().update(true, true);

                break;
            }
        }
    }

    public void run(Block block, final int mode, final int length, Consumer<BoundingBox> consumer) {
        for (int i = -length; i <= length; i++) {

            Location location = block.getLocation().clone();

            switch (mode) {
                case 0 -> location.add(i, 0, 0);
                case 1 -> location.add(0, i, 0);
                case 2 -> location.add(0, 0, i);
            }

            if (location.getBlock().getType().isAir()) {
                consumer.accept(new BoundingBox(location.getX(), location.getY(), location.getZ(), location.getX() + 1, location.getY() + 1, location.getZ() + 1));
            }
        }
    }

    public int getTotal(List<Block> blocks) {
        int totals = 0;

        for (Block block : blocks) {
            if (block.getType() == Material.AIR) totals++;
        }

        return totals;
    }
}
