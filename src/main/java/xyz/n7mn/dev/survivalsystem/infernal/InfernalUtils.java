package xyz.n7mn.dev.survivalsystem.infernal;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Entity;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;

@UtilityClass
public class InfernalUtils {
    public <T> void addInfernal(Entity entity, InfernalManager.Infernal infernal, T value) {
        switch (infernal) {
            case QUICK_SHOT -> SurvivalInstance.INSTANCE.getInfernalManager().quickShot.register(entity, value);
        }
    }
}
