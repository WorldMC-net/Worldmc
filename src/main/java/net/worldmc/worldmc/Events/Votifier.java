package net.worldmc.worldmc.Events;

import net.worldmc.worldmc.Utilities.VoteReward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.vexsoftware.votifier.model.VotifierEvent;

public class Votifier implements Listener {

    @EventHandler
    public void votifierEvent(VotifierEvent event) {
        com.vexsoftware.votifier.model.Vote vote = event.getVote();
        Player player = Bukkit.getPlayer(vote.getUsername());

        VoteReward.reward(player);
    }
}
