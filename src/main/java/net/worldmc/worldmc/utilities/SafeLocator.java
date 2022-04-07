package net.worldmc.worldmc.utilities;

import net.worldmc.worldmc.Worldmc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class SafeLocator {

    public static CompletableFuture<Location> findSafeLocation(World world) {
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
                    if (!RandomTeleport.safeBlocks.contains(highestBlock.getType())) {
                        Bukkit.getScheduler().runTaskAsynchronously(Worldmc.getInstance(), this);
                        return;
                    }
                    newLocation.completeAsync(highestBlock::getLocation);
                });
            }
        });

        return newLocation;
    }
}
