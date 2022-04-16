package net.worldmc.worldmc.Events;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.Database.MySQL;
import net.worldmc.worldmc.Utilities.RandomTeleport;
import net.worldmc.worldmc.Utilities.SendService;
import net.worldmc.worldmc.Utilities.TabSorter;
import net.worldmc.worldmc.Utilities.WelcomeReward;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        MySQL.createPlayer(player.getUniqueId());
        TabSorter.setTab();

        if (!player.hasPlayedBefore()) {
            WelcomeReward.newPlayer(player);
            RandomTeleport.issueTeleport(player);
            MySQL.setGhost(player.getUniqueId(), true);
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }

        if (MySQL.getGhost(player.getUniqueId())) {
            RandomTeleport.issueTeleport(player);
            Bukkit.getScheduler().runTaskLater(Worldmc.getInstance(), () -> {
                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Finding"));
            }, 1L);
        }
    }
}