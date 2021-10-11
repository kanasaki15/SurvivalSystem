package xyz.n7mn.dev.survivalsystem.event;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
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
import org.bukkit.event.player.*;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.Plugin;


public class EventListener implements Listener {

    private Plugin plugin = Bukkit.getPluginManager().getPlugin("SurvivalSystem");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AsyncChatEvent (AsyncChatEvent e){
        Component message = e.message();

        e.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
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
                TextComponent text1 = Component.text("" +
                        "ななみ生活鯖\n接続可能バージョン : 1.16.4 - " + plugin.getServer().getMinecraftVersion() + "\n" +
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerLoginEvent (PlayerLoginEvent e){
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        int version = protocolManager.getProtocolVersion(e.getPlayer());

        if (version < 754){
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("Outdated server! I'm still on 1.16.4-"+plugin.getServer().getMinecraftVersion()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerJoinEvent (PlayerJoinEvent e){
        e.joinMessage(Component.text(""));

        for (Player player : plugin.getServer().getOnlinePlayers()){
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void TabCompleteEvent (TabCompleteEvent e){
        if (e.getSender() instanceof Player){
            Player player = (Player) e.getSender();
        }

        System.out.println(e.getBuffer());
    }
}
