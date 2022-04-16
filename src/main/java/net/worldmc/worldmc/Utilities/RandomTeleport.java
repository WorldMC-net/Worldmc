package net.worldmc.worldmc.Utilities;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.Database.MySQL;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class RandomTeleport {
    public static HashSet<Material> safeBlocks = new HashSet<>();
    private static final HashSet<UUID> undergoingTeleport = new HashSet<>();

    public static void issueTeleport(Player player) {
        if (undergoingTeleport.contains(player.getUniqueId())) {
            return;
        }
        undergoingTeleport.add(player.getUniqueId());
        SafeLocator.findSafeLocation().thenAccept(location -> {
            Location finalLocation = location.add(0, 1, 0);
            Bukkit.getScheduler().runTask(Worldmc.getInstance(), () -> {
                undergoingTeleport.remove(player.getUniqueId());
                if (player.isOnline()) {
                    MySQL.setGhost(player.getUniqueId(), false);
                    player.teleportAsync(finalLocation);
                    sendLocations(player, finalLocation);
                    player.setGameMode(GameMode.SURVIVAL);
                }
            });
        });
    }

    private static void sendLocations(Player player, Location location) {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("x", Integer.toString(location.getBlockX()));
        placeholders.put("y", Integer.toString(location.getBlockY()));
        placeholders.put("z", Integer.toString(location.getBlockZ()));

        SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("RandomTeleport.Messages.Random"), placeholders);

        player.teleportAsync(location);
        player.playSound(location, Sound.valueOf(Worldmc.getInstance().getConfig().getString("RandomTeleport.TeleportSound")), 1, 1);
    }
}
