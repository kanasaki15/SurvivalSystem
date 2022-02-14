package xyz.n7mn.dev.survivalsystem.timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.cache.GraveCache;
import xyz.n7mn.dev.survivalsystem.itemchecker.InventoryItemChecker;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;
import xyz.n7mn.dev.survivalsystem.util.MessageManager;
import xyz.n7mn.dev.survivalsystem.util.PlayerDataUtil;

import java.security.SecureRandom;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Timer {
    private long systemTime;

    public static long serverLagSpike;
    private BukkitTask bukkitTask;
    private int announceTime;

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(SurvivalInstance.INSTANCE.getPlugin(), () -> {

            long now = System.currentTimeMillis() - systemTime - 1000;

            if (now < 0) now = 0;

            serverLagSpike = now;

            this.systemTime = System.currentTimeMillis();

            handle();
        }, 0, 20);

    }

    private void handle() {
        Bukkit.getOnlinePlayers().forEach(onlinePlayers -> {
            PlayerData data = PlayerDataUtil.getPlayerData(onlinePlayers);

            data.getVanishData().handle();

            InventoryItemChecker itemCheck = SurvivalInstance.INSTANCE.getItemChecker();

            if (itemCheck != null) itemCheck.tasks(onlinePlayers);
        });

        announce();

        GraveCache.handle();
    }

    private void announce() {
        if (announceTime++ > SurvivalInstance.INSTANCE.getPlugin().getConfig().getInt("announceTime")) {
            List<String> list = MessageManager.getStringList("TipsList");

            int pick = new SecureRandom().nextInt(list.size());

            for (Player player : getServer().getOnlinePlayers()) {
                player.sendMessage(ChatColor.YELLOW + "[ななみ鯖 Tips] " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', list.get(pick)));
            }

            announceTime = 0;
        }
    }

    public void stop() {
        if (bukkitTask != null) bukkitTask.cancel();
    }
}