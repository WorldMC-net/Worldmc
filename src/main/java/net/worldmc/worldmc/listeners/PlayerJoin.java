package net.worldmc.worldmc.listeners;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.database.MySQL;
import net.worldmc.worldmc.utilities.RandomTeleport;
import net.worldmc.worldmc.utilities.SendService;
import net.worldmc.worldmc.utilities.TabSorter;
import net.worldmc.worldmc.utilities.WelcomeReward;
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
        }

        if (player.isDead()) {
            RandomTeleport.issueTeleport(player);
        }

        if (player.getGameMode() == GameMode.SPECTATOR) {
            RandomTeleport.issueTeleport(player);
            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Finding"));
        }
    }
}