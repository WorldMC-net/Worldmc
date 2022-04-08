package net.worldmc.worldmc.utilities;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.worldmc.worldmc.Worldmc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class RandomTeleport {
    public static HashSet<Material> safeBlocks = new HashSet<>();
    public static final HashMap<UUID, Location> toRespawn = new HashMap<>();
    private static final HashSet<UUID> undergoingTeleport = new HashSet<>();

    public static void issueTeleport(Player player) {
        if (toRespawn.containsKey(player.getUniqueId())) {
            sendLocations(player, toRespawn.get(player.getUniqueId()));
            toRespawn.remove(player.getUniqueId());
            return;
        }
        if (undergoingTeleport.contains(player.getUniqueId())) {
            return;
        }
        undergoingTeleport.add(player.getUniqueId());
        SafeLocator.findSafeLocation().thenAccept(location -> {
            Location finalLocation = location.add(0, 1, 0);
            Bukkit.getScheduler().runTask(Worldmc.getInstance(), () -> {
                if (player.isDead()) {
                    toRespawn.put(player.getUniqueId(), finalLocation);
                } else {
                    player.teleportAsync(finalLocation);
                    sendLocations(player, finalLocation);
                }
                undergoingTeleport.remove(player.getUniqueId());
            });
        });
    }

    private static void sendLocations(Player player, Location location) {
        TagResolver.Single x = Placeholder.parsed("x", Integer.toString(location.getBlockX()));
        TagResolver.Single y = Placeholder.parsed("y", Integer.toString(location.getBlockY()));
        TagResolver.Single z = Placeholder.parsed("z", Integer.toString(location.getBlockZ()));
        TagResolver placeholders = TagResolver.resolver(x, y, z);

        SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("RandomTeleport.Messages.Random"), placeholders);

        player.teleportAsync(location);
        player.playSound(location, Sound.valueOf(Worldmc.getInstance().getConfig().getString("RandomTeleport.TeleportSound")), 1, 1);
    }
}
