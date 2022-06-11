package xyz.n7mn.dev.survivalsystem.customcraft.item;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.itemchecker.base.TickCheck;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;
import xyz.n7mn.dev.survivalsystem.util.ParticleUtils;
import xyz.n7mn.dev.survivalsystem.util.PlayerDataUtil;

import java.util.List;

public class BuildersWand implements TickCheck, Listener {

    //todo: persistent data container
    private final int length = 5;

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
            }
        }
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
                    run(block, 0, length, box -> box.getCenter().toLocation(e.getClickedBlock().getWorld()).getBlock().setType(Material.STONE));
                } else if (dirY > dirZ) {
                    run(block, 1, length, box -> box.getCenter().toLocation(e.getClickedBlock().getWorld()).getBlock().setType(Material.STONE));
                } else {
                    run(block, 2, length, box -> box.getCenter().toLocation(e.getClickedBlock().getWorld()).getBlock().setType(Material.STONE));
                }

                data.getEventData().setLastBuildersWand(System.currentTimeMillis());
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
