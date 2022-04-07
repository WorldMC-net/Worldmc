package net.worldmc.worldmc.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.worldmc.worldmc.Worldmc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendService {
    private static final String prefix = Worldmc.getInstance().getConfig().getString("Prefix");

    public static void sendMessage(Player player, String message) {
        if (message.isEmpty()) { return; }
        Component parsed = MiniMessage.miniMessage().deserialize(prefix + message);
        player.sendMessage(parsed);
    }

    public static void sendMessage(Player player, String message, TagResolver placeholders) {
        if (message.isEmpty()) { return; }
        Component parsed = MiniMessage.miniMessage().deserialize(prefix + message, placeholders);
        player.sendMessage(parsed);
    }

    public static void sendBroadcast(String broadcast) {
        Component parsed = MiniMessage.miniMessage().deserialize(prefix + broadcast);
        Bukkit.broadcast(parsed);
    }

    public static void sendBroadcast(String broadcast, TagResolver placeholders) {
        Component parsed = MiniMessage.miniMessage().deserialize(prefix + broadcast, placeholders);
        Bukkit.broadcast(parsed);
    }

    public static void sendCommand(String command) {
        Component parsed = MiniMessage.miniMessage().deserialize(command);
        String serialized = MiniMessage.miniMessage().serialize(parsed);
        Bukkit.dispatchCommand(Worldmc.getInstance().getServer().getConsoleSender(), serialized);
    }

    public static void sendCommand(String command, TagResolver placeholders) {
        Component parsed = MiniMessage.miniMessage().deserialize(command, placeholders);
        String serialized = MiniMessage.miniMessage().serialize(parsed);
        Bukkit.dispatchCommand(Worldmc.getInstance().getServer().getConsoleSender(), serialized);
    }
}
