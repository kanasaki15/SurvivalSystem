package xyz.n7mn.dev.survivalsystem.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.survivalsystem.playerdata.PlayerData;
import xyz.n7mn.dev.survivalsystem.util.MessageManager;
import xyz.n7mn.dev.survivalsystem.util.MessageUtil;
import xyz.n7mn.dev.survivalsystem.util.PlayerDataUtil;
import xyz.n7mn.dev.survivalsystem.util.VanishManager;

public class VanishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            final boolean hasData = VanishManager.hasData(player);

            PlayerData data = PlayerDataUtil.getPlayerData(player);
            data.getVanishData().setVanished(!hasData);

            if (hasData) {
                VanishManager.remove(player);

                player.sendMessage(Component.text(MessageUtil.replaceFromConfig("VANISH-OFF")));
                MessageUtil.sendMessageBroadCast("JOIN-MESSAGE", "%player%|" + player.getName());

                VanishManager.showPlayer(player);
            } else {
                VanishManager.add(player);

                player.sendMessage(Component.text(MessageUtil.replaceFromConfig("VANISH-ON")));
                MessageUtil.sendMessageBroadCast("QUIT-MESSAGE", "%player%|" + player.getName());

                VanishManager.handleHide(player);
            }
        } else {
            sender.sendMessage(MessageManager.getString("CANNOT-EXECUTE-FROM-CONSOLE"));
        }
        return true;
    }
}
