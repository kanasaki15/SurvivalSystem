package xyz.n7mn.dev.survivalsystem.customenchant.itemcheck;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.n7mn.dev.survivalsystem.customenchant.CustomEnchantUtils;
import xyz.n7mn.dev.survivalsystem.itemchecker.base.TickCheck;
import xyz.n7mn.dev.survivalsystem.util.SyncUtil;

public class NightVisionEnchantCheck implements TickCheck {

    @Override
    public void onTick(Player player) {
        if (player.getEquipment().getHelmet() != null) {

            final int level = player.getEquipment().getHelmet().getEnchantLevel(CustomEnchantUtils.NIGHT_VISION);

            if (level != 0) {
                SyncUtil.addPotionEffect(player, new PotionEffect(PotionEffectType.NIGHT_VISION, 500 * level, level - 1, true));
            }
        }
    }
}