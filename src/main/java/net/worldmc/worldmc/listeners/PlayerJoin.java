package net.worldmc.worldmc.listeners;

import net.worldmc.worldmc.database.MySQL;
import net.worldmc.worldmc.utilities.NewPlayerProtection;
import net.worldmc.worldmc.utilities.TabSorter;
import net.worldmc.worldmc.utilities.WelcomeReward;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        NewPlayerProtection.checkEnabledProtection(player);
        MySQL.createPlayer(player.getUniqueId());
        TabSorter.setTab();

        if (!player.hasPlayedBefore()) {
            WelcomeReward.newPlayer(player);
        }
    }
}