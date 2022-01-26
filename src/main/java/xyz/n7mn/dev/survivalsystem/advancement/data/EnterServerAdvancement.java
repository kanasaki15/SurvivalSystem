package xyz.n7mn.dev.survivalsystem.advancement.data;

import net.roxeez.advancement.Advancement;
import net.roxeez.advancement.AdvancementCreator;
import net.roxeez.advancement.Context;
import net.roxeez.advancement.display.BackgroundType;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class EnterServerAdvancement implements AdvancementCreator {
    public static final String ID = "enter_server";

    @Override
    public @NotNull Advancement create(@NotNull Context context) {
        Advancement advancement = new Advancement(context.getPlugin(), ID);

        advancement.setDisplay(show -> {
            show.setTitle("生活鯖");
            show.setDescription("初めて入室する");
            show.setBackground(BackgroundType.SMOOTH_STONE);
            show.setIcon(Material.GRASS_BLOCK);
        });

        advancement.addCriteria("enter_server", TriggerType.TICK, trigger -> {
            //文字通り何もなし
        });

        return advancement;
    }
}
