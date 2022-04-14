package net.worldmc.worldmc.listeners;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.utilities.RandomTeleport;
import net.worldmc.worldmc.utilities.SendService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

public class PlayerRespawn implements Listener {

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (player.getBedSpawnLocation() != null) {
            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("RandomTeleport.Messages.Bed"));
            return;
        }

        /** if (TownyAPI.getInstance().getTownSpawnLocation(player) != null) {
            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("RandomTeleport.Messages.Town"));
            event.setRespawnLocation(TownyAPI.getInstance().getTownSpawnLocation(player));
            return;
        } */

        RandomTeleport.issueTeleport(player);
        SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Finding"));
    }
}
