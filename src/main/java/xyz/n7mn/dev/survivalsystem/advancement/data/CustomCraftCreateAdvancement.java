package xyz.n7mn.dev.survivalsystem.advancement.data;

import net.roxeez.advancement.Advancement;
import net.roxeez.advancement.AdvancementCreator;
import net.roxeez.advancement.Context;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.advancement.reward.AdvancementReward;
import xyz.n7mn.dev.survivalsystem.util.ItemStackUtil;

public class CustomCraftCreateAdvancement implements AdvancementCreator, AdvancementReward {
    public static final String ID = "custom_craft_create";

    @Override
    public @NotNull Advancement create(@NotNull Context context) {
        Advancement advancement = new Advancement(context.getPlugin(), ID);

        advancement.setParent(new NamespacedKey(context.getPlugin(), CustomCraftOpenAdvancement.ID));

        advancement.setDisplay(show -> {
            show.setTitle("カスタム作業台で何か作る");
            show.setDescription("カスタム作業台でレシピを確認できるそうですよ！");
            show.setIcon(Material.CRAFTING_TABLE);
        });

        advancement.addCriteria("grant", TriggerType.IMPOSSIBLE, trigger -> {
            //何もなし
        });

        return advancement;
    }

    @Override
    public void reward(Player player) {
        ItemStackUtil.addItem(player, ItemStackUtil.createItem(Material.IRON_INGOT, 5));
    }
}