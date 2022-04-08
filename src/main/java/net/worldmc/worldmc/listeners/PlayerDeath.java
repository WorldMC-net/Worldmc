package net.worldmc.worldmc.listeners;

import net.worldmc.worldmc.utilities.RandomTeleport;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (player.getBedSpawnLocation() != null) {
            return;
        }

        RandomTeleport.issueTeleport(player);
    }
}
