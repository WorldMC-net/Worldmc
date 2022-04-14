package net.worldmc.worldmc.listeners;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.database.MySQL;
import net.worldmc.worldmc.utilities.RandomTeleport;
import net.worldmc.worldmc.utilities.SendService;
import org.bukkit.Bukkit;
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
        SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("RandomTeleport.Messages.Died"));
    }
}
