package xyz.n7mn.dev.survivalsystem.infernal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public abstract class InfernalAbstract {
    public abstract void handle(LivingEntity entity);

    public abstract void register(Entity entity, Object... objects);

}
