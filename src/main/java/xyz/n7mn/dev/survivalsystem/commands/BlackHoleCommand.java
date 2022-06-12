package xyz.n7mn.dev.survivalsystem.commands;

import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.util.ParticleUtils;

public class BlackHoleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            ParticleUtils.summonSphereParticle(player, player.getLocation(), 1.7, Particle.VILLAGER_HAPPY);
        }

        return false;
    }
}
