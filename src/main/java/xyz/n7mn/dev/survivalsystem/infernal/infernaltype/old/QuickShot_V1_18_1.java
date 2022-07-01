package xyz.n7mn.dev.survivalsystem.infernal.infernaltype.old;

import org.bukkit.craftbukkit.v1_19_R1.entity.CraftSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataType;
import xyz.n7mn.dev.survivalsystem.infernal.InfernalAbstract;

import static xyz.n7mn.dev.survivalsystem.infernal.InfernalNameSpace.quickShotNameSpace;

//todo: replace
public class QuickShot_V1_18_1 extends InfernalAbstract {

    @Override
    public void handle(LivingEntity entity) {

        ((CraftSkeleton) entity).getHandle().setSecondsOnFire(0, true);

        /*try {
            Field field = EntitySkeletonAbstract.class.getDeclaredField("b");
            field.setAccessible(true);

            PathfinderGoalBowShoot<EntitySkeletonAbstract> abstractPathfinderGoalBowShoot = (PathfinderGoalBowShoot<EntitySkeletonAbstract>) field.get(entitySkeleton);

            //maybe replaced
            Field speed = PathfinderGoalBowShoot.class.getDeclaredField("b");
            speed.setAccessible(true);

            speed.setDouble(abstractPathfinderGoalBowShoot, 3); // target speed

            Field itemCoolDown = EntityLiving.class.getDeclaredField("bB");
            itemCoolDown.setAccessible(true);
            itemCoolDown.setInt(entitySkeleton, -100);

            //replaced
            Field item = EntityLiving.class.getDeclaredField("bz");
            item.setAccessible(true);
            item.set(entitySkeleton, CraftItemStack.asNMSCopy(entity.getEquipment().getItemInMainHand()));

            Method method = EntityLiving.class.getDeclaredMethod("c", int.class, boolean.class);
            method.setAccessible(true);
            method.invoke(entitySkeleton, 1, true);

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
        }*/
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