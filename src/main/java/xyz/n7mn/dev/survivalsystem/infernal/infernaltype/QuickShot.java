package xyz.n7mn.dev.survivalsystem.infernal.infernaltype;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoalBowShoot;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.monster.EntitySkeletonAbstract;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataType;
import xyz.n7mn.dev.survivalsystem.infernal.InfernalAbstract;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static xyz.n7mn.dev.survivalsystem.infernal.InfernalNameSpace.quickShotNameSpace;

public class QuickShot extends InfernalAbstract {

    @Override
    public void handle(LivingEntity entity) {

        EntitySkeleton entitySkeleton = ((CraftSkeleton) entity).getHandle();

        try {
            Field field = EntitySkeletonAbstract.class.getDeclaredField("b");
            field.setAccessible(true);

            PathfinderGoalBowShoot<EntitySkeletonAbstract> abstractPathfinderGoalBowShoot = (PathfinderGoalBowShoot<EntitySkeletonAbstract>) field.get(entitySkeleton);

            Field speed = PathfinderGoalBowShoot.class.getDeclaredField("b");
            speed.setAccessible(true);

            speed.setDouble(abstractPathfinderGoalBowShoot, 3); // target speed

            Field itemCoolDown = EntityLiving.class.getDeclaredField("bB");
            itemCoolDown.setAccessible(true);
            itemCoolDown.setInt(entitySkeleton, -100);

            Field item = EntityLiving.class.getDeclaredField("bA");
            item.setAccessible(true);
            item.set(entitySkeleton, CraftItemStack.asNMSCopy(entity.getEquipment().getItemInMainHand()));

            Method method = EntityLiving.class.getDeclaredMethod("c", int.class, boolean.class);
            method.setAccessible(true);
            method.invoke(entitySkeleton, 1, true);

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void register(Entity entity, Object... objects) {
        if (entity instanceof Skeleton skeleton && objects[0] instanceof Integer integer) {
            skeleton.getPersistentDataContainer().set(quickShotNameSpace, PersistentDataType.INTEGER, integer);

            handle(skeleton);
        } else {
            throw new RuntimeException();
        }
    }
}