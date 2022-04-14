package net.worldmc.worldmc.listeners;

import net.worldmc.worldmc.utilities.TabSorter;
import net.worldmc.worldmc.utilities.WelcomeReward;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        WelcomeReward.playerQuit(player);
        TabSorter.setTab();
    }
}