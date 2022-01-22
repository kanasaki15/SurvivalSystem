package xyz.n7mn.dev.survivalsystem.event;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.SurvivalInstance;
import xyz.n7mn.dev.survivalsystem.util.MessageManager;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;
import xyz.n7mn.dev.survivalsystem.util.PlayerDataUtil;


public class EventListener implements Listener {

    @EventHandler
    public void AsyncChatEvent (AsyncChatEvent e){
        Component message = e.message();

        e.setCancelled(true);
    }


    @EventHandler
    public void PlayerCommandPreprocessEvent (PlayerCommandPreprocessEvent e){

        // op以外には見せたくないものとか
        if (!e.getPlayer().isOp()){
            if (e.getMessage().startsWith("/help")){
                e.setMessage("/rule");
            }

            if (e.getMessage().equals("/pl") || e.getMessage().startsWith("/plugins")){
                e.getPlayer().sendMessage("Plugins (0):");
                e.setCancelled(true);
            }

            if (e.getMessage().equals("/stop")){
                e.setCancelled(true);
            }

            if (e.getMessage().startsWith("/version") || e.getMessage().startsWith("/ver ") || e.getMessage().equals("/ver")){

                Plugin plugin = SurvivalInstance.INSTANCE.getPlugin();

                TextComponent text1 = Component.text("" +
                        "ななみ生活鯖\n接続可能バージョン : 1.16 - " + plugin.getServer().getMinecraftVersion() + "\n" +
                        "This server is running NanamiSurvivalServer\nConnectable Versions : 1.16.4 - " + plugin.getServer().getMinecraftVersion() + "\n\n" +
                        "不具合報告はDiscordの「#せいかつ鯖-不具合報告」へどうぞ。\n" +
                        "If you want to report a bug, please go to \"#せいかつ鯖-不具合報告\" on Discord.\n"
                );
                TextComponent text2 = Component.text(ChatColor.BLUE + "[Discord]").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, plugin.getConfig().getString("DiscordURL")));
                text1.append(text2);

                e.getPlayer().sendMessage(text1);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerLoginEvent (PlayerLoginEvent e) {
        @NotNull ClientVersion version = PacketEvents.get().getPlayerUtils().getClientVersion(e.getPlayer());

        if (version.isOlderThan(ClientVersion.v_1_16)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text(MessageUtil.replaceString("OUT-DATED-MESSAGE", "%version%|" + Bukkit.getVersion())));
        } else {
            PlayerDataUtil.putPlayerData(e.getPlayer());

            MessageUtil.sendMessageBroadCast("JOIN-MESSAGE", "%player%|" + e.getPlayer().getName());
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        if (!PlayerDataUtil.removePlayerData(e.getPlayer()).getVanishManager().isVanished()) {
            MessageUtil.sendMessageBroadCast("QUIT-MESSAGE", "%player%|" + e.getPlayer().getName());
        }
    }

    @EventHandler
    public void PlayerJoinEvent (PlayerJoinEvent e){
        e.joinMessage(Component.empty());

        for (Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }
    }
}
