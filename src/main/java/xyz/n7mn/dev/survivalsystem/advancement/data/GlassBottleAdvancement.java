package xyz.n7mn.dev.survivalsystem.advancement.data;

import net.roxeez.advancement.Advancement;
import net.roxeez.advancement.AdvancementCreator;
import net.roxeez.advancement.Context;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.itemchecker.base.ItemCheck;

public class GlassBottleAdvancement extends ItemCheck implements AdvancementCreator {

    public static final String ID = "glass_bottle";

    @Override
    public @NotNull Advancement create(@NotNull Context context) {
        Advancement advancement = new Advancement(context.getPlugin(), ID);

        advancement.setParent(new NamespacedKey(context.getPlugin(), EnterServerAdvancement.ID));

        advancement.setDisplay(show -> {
            show.setTitle("ガラス瓶を入手する");
            show.setDescription("ガラス瓶を入手してみよう");
            show.setIcon(Material.GLASS_BOTTLE);
        });

        //なぜかバグってる
        advancement.addCriteria("grant", TriggerType.IMPOSSIBLE, trigger -> {
            //文字通り何もなし
        });

        return advancement;
    }

    @Override
    public void item(Player player, ItemStack itemStack) {
        if (itemStack.getType() == Material.GLASS_BOTTLE) {
            grant(player, ID);
        }
    }
}