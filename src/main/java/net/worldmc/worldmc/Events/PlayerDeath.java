package net.worldmc.worldmc.Events;

import net.worldmc.worldmc.Database.MySQL;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (player.getBedSpawnLocation() != null/** || TownyAPI.getInstance().getTownSpawnLocation(player) != null*/) {
            return;
        }

        MySQL.setGhost(player.getUniqueId(), true);
        player.setGameMode(GameMode.SPECTATOR);
    }
}
