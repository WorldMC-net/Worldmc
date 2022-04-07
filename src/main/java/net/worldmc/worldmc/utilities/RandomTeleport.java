package net.worldmc.worldmc.utilities;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.worldmc.worldmc.Worldmc;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class RandomTeleport {
    public static HashSet<Material> safeBlocks = new HashSet<>();

    public static void createRTP(Player player, World world) {
        SafeLocator.findSafeLocation(world).thenAccept(location -> {
            Location finalLocation = location.add(0, 1, 0);
            Bukkit.getScheduler().runTask(Worldmc.getInstance(), () -> issueTeleport(player, finalLocation));
        });
    }

    private static void issueTeleport(Player player, Location location) {
        TagResolver.Single x = Placeholder.parsed("x", Integer.toString(location.getBlockX()));
        TagResolver.Single y = Placeholder.parsed("y", Integer.toString(location.getBlockY()));
        TagResolver.Single z = Placeholder.parsed("z", Integer.toString(location.getBlockZ()));
        TagResolver placeholders = TagResolver.resolver(x, y, z);

        SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("RandomTeleport.Messages.Random"), placeholders);

        player.teleportAsync(location);
        player.playSound(location, Sound.valueOf(Worldmc.getInstance().getConfig().getString("RandomTeleport.TeleportSound")), 1, 1);
    }
}
