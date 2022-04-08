package net.worldmc.worldmc.utilities;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.database.MySQL;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VoteReward {

    public static void reward(Player player) {
        if (!Worldmc.getInstance().getConfig().getBoolean("VoteReward.Enabled")) {
            return;
        }

        MySQL.addVote(player.getUniqueId());

        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());

        SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("VoteReward.Messages.Voted"));
        SendService.sendCommand(Worldmc.getInstance().getConfig().getString("VoteReward.Command"), placeholders);

        player.playSound(player.getLocation(), Sound.valueOf(Worldmc.getInstance().getConfig().getString("VoteReward.RewardSound")), 1, 1);

        for (String section : Worldmc.getInstance().getConfig().getConfigurationSection("VoteReward.Milestones").getKeys(false)) {
            if ((MySQL.getVotes(player.getUniqueId()) % Integer.parseInt(section) * 4) == 0) {
                SendService.sendBroadcast(Worldmc.getInstance().getConfig().getString("VoteReward.Milestones." + section + ".Broadcast"), placeholders);
                SendService.sendCommand(Worldmc.getInstance().getConfig().getString("VoteReward.Milestones." + section + ".Command"), placeholders);
            }
        }
    }
}
