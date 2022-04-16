package net.worldmc.worldmc.Utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.worldmc.worldmc.Worldmc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SendService {
    private static final String prefix = Worldmc.getInstance().getConfig().getString("Prefix");

    public static void sendMessage(Player player, String message) {
        if (message.isEmpty()) { return; }
        Component component = MiniMessage.get().parse(prefix + message);
        player.sendMessage(component);
    }

    public static void sendMessage(Player player, String message, HashMap<String, String> placeholders) {
        if (message.isEmpty()) { return; }
        Component component = MiniMessage.get().parse(prefix + message, placeholders);
        player.sendMessage(component);
    }

    public static void sendBroadcast(String broadcast) {
        Component component = MiniMessage.get().parse(prefix + broadcast);
        Bukkit.broadcast(component);
    }

    public static void sendBroadcast(String broadcast, HashMap<String, String> placeholders) {
        Component component = MiniMessage.get().parse(prefix + broadcast, placeholders);
        Bukkit.broadcast(component);
    }

    public static void sendCommand(String command) {
        Component component = MiniMessage.get().parse(command);
        String serialize = MiniMessage.get().serialize(component);
        Bukkit.dispatchCommand(Worldmc.getInstance().getServer().getConsoleSender(), serialize);
    }

    public static void sendCommand(String command, HashMap<String, String> placeholders) {
        Component component = MiniMessage.get().parse(command, placeholders);
        String serialize = MiniMessage.get().serialize(component);
        Bukkit.dispatchCommand(Worldmc.getInstance().getServer().getConsoleSender(), serialize);
    }
}

