package net.worldmc.worldmc.utilities;

import net.worldmc.worldmc.Worldmc;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class WelcomeReward {
    private static final HashSet<UUID> completedWelcome = new HashSet<>();
    private static Player newPlayer;

    public static void newPlayer(Player player) {
        if (!Worldmc.getInstance().getConfig().getBoolean("WelcomeReward.Enabled")) {
            return;
        }

        completedWelcome.clear();
        newPlayer = player;

        Bukkit.getScheduler().runTaskLater(Worldmc.getInstance(), () -> {
            if (newPlayer != null) {
                completedWelcome.clear();
                newPlayer = null;
            }
        }, Worldmc.getInstance().getConfig().getInt("WelcomeReward.TimeLimit") * 20L);
    }

    public static void playerWelcome(Player player) {
        if (newPlayer != null && newPlayer != player && !completedWelcome.contains(player.getUniqueId())) {
            completedWelcome.add(player.getUniqueId());

            HashMap<String, String> placeholders = new HashMap<>();
            placeholders.put("player", player.getName());
            placeholders.put("target", newPlayer.getName());

            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("WelcomeReward.Message"), placeholders);
            SendService.sendCommand(Worldmc.getInstance().getConfig().getString("WelcomeReward.Command"), placeholders);

            player.playSound(player.getLocation(), Sound.valueOf(Worldmc.getInstance().getConfig().getString("WelcomeReward.RewardSound")), 1, 1);
        }
    }

    public static void playerQuit(Player player) {
        if (newPlayer == player) {
            completedWelcome.clear();
            newPlayer = null;
        }
    }
}
