package net.worldmc.worldmc.utilities;

import com.palmergames.bukkit.towny.TownyAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.worldmc.worldmc.Worldmc;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class RandomTeleport {
    public static HashSet<Material> safeBlocks = new HashSet<>();

    public static void randomTeleport(Player player, World world) {
        CompletableFuture<Location> newLocation = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(Worldmc.getInstance(), new Runnable() {
            int attempts = 0;
            @Override
            public void run() {
                if (attempts >= Worldmc.getInstance().getConfig().getInt("RandomTeleport.MaxAttempts")) {
                    newLocation.completeAsync(null);
                    return;
                }
                attempts ++;
                int x = new Random().nextInt(Worldmc.getInstance().getConfig().getInt("RandomTeleport.X")) * (new Random().nextBoolean() ? -1 : 1);
                int z = new Random().nextInt(Worldmc.getInstance().getConfig().getInt("RandomTeleport.Z")) * (new Random().nextBoolean() ? -1 : 1);

                world.getChunkAtAsync(new Location(world, x, 0, z)).thenAccept(chunk -> {
                    Block highestBlock = world.getHighestBlockAt(x, z);
                    if (!safeBlocks.contains(highestBlock.getType()) && TownyAPI.getInstance().isWilderness(highestBlock)) {
                        Bukkit.getScheduler().runTaskAsynchronously(Worldmc.getInstance(), this);
                        return;
                    }
                    newLocation.completeAsync(highestBlock::getLocation);
                });
            }
        });

        newLocation.thenAccept(location -> {
            Location finalLocation = location.add(0, 1, 0);
            Bukkit.getScheduler().runTask(Worldmc.getInstance(), () -> {
                TagResolver.Single x = Placeholder.parsed("x", Integer.toString(finalLocation.getBlockX()));
                TagResolver.Single y = Placeholder.parsed("y", Integer.toString(finalLocation.getBlockY()));
                TagResolver.Single z = Placeholder.parsed("z", Integer.toString(finalLocation.getBlockZ()));
                TagResolver placeholders = TagResolver.resolver(x, y, z);

                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("RandomTeleport.Messages.Random"), placeholders);

                player.teleportAsync(finalLocation);
                player.playSound(finalLocation, Sound.valueOf(Worldmc.getInstance().getConfig().getString("RandomTeleport.TeleportSound")), 1, 1);
            });
        });
    }
}
