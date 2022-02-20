package xyz.n7mn.dev.survivalsystem.infernal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.persistence.PersistentDataType;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.infernal.infernaltype.QuickShot;

import static xyz.n7mn.dev.survivalsystem.infernal.InfernalNameSpace.quickShotNameSpace;

public class InfernalManager implements Listener {

    //TODO: 1.19が来たらアップデートする

    public void init() {
        Bukkit.getPluginManager().registerEvents(this, SurvivalInstance.INSTANCE.getPlugin());

        InfernalNameSpace.init();

        quickShot = new QuickShot();
    }

    public QuickShot quickShot;

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Skeleton && e.getEntity().getPersistentDataContainer().has(quickShotNameSpace, PersistentDataType.INTEGER)) {

            final int bow = e.getEntity().getPersistentDataContainer().get(quickShotNameSpace, PersistentDataType.INTEGER);

            if (bow != -1) {
                Bukkit.getScheduler().runTaskLater(SurvivalInstance.INSTANCE.getPlugin(), () -> quickShot.handle(e.getEntity()), bow);
            } else {
                quickShot.handle(e.getEntity());
            }
        }

    }

    public enum Infernal {
        QUICK_SHOT,
    }
}