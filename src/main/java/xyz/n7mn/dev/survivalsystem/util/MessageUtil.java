package xyz.n7mn.dev.survivalsystem.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MessageUtil {
    public String translateAlternateColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void sendChat(Player player, String messageID, String... replace) {
        String message = replaceString(messageID, replace).replaceAll("%player%", player.getName());

        player.sendMessage(message);
    }

    public void sendChat(String messageID, String... replace) {
        for (Player player : Bukkit.getOnlinePlayers()) {

            String message = replaceString(messageID, replace).replaceAll("%player%", player.getName());

            player.sendMessage(message);
        }
    }

    public void sendMessageBroadCast(String messageID, String... replace) {
        String message = replaceString(messageID, replace);

        Bukkit.broadcast(Component.text(message));
    }

    public String replaceString(String messageID, String... replace) {

        String message = MessageManager.getString(messageID);

        if (replace == null) return message;

        for (String str : replace) {
            String[] split = str.split("\\|", 2);

            message = message.replaceAll(split[0], split[1]);

        }
        return message;
    }

    public String replaceStringList(String messageID, String... replace) {

        StringBuilder stringBuilder = new StringBuilder();

        List<String> list = MessageManager.getStringList(messageID);

        if (replace == null) return MessageUtil.translateAlternateColorCodes(String.valueOf(list));

        for (String message : list) {
            for (String str : replace) {
                String[] split = str.split("\\|", 2);

                message = translateAlternateColorCodes(message.replaceAll(split[0], split[1]));
            }

            if (stringBuilder.length() == 0) stringBuilder.append(message);
            else stringBuilder.append("\n").append(message);
        }

        return stringBuilder.toString();
    }

    public List<String> replaceList(String messageID, String... replace) {

        List<String> list = MessageManager.getStringList(messageID);

        if (replace == null) return list;
        List<String> temp = new ArrayList<>();

        for (String message : list) {
            for (String str : replace) {
                String[] split = str.split("\\|", 2);

                message = MessageUtil.translateAlternateColorCodes(message.replaceAll(split[0], split[1]));
            }

            temp.add(message);
        }

        return temp;
    }
}