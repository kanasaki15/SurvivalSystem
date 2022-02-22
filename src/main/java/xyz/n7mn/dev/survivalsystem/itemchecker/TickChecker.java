package xyz.n7mn.dev.survivalsystem.itemchecker;

import org.bukkit.entity.Player;
import xyz.n7mn.dev.survivalsystem.customenchant.itemcheck.NightVisionEnchantCheck;
import xyz.n7mn.dev.survivalsystem.itemchecker.base.TickCheck;

import java.util.ArrayList;
import java.util.List;

public class TickChecker {
    private final List<TickCheck> tickChecks = new ArrayList<>();

    public void init() {
        register(new NightVisionEnchantCheck());
    }

    public void register(TickCheck tickCheck) {
        tickChecks.add(tickCheck);
    }

    public void forEach(Player player) {
        tickChecks.forEach(tick -> tick.onTick(player));
    }
}
