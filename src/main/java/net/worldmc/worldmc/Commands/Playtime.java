package net.worldmc.worldmc.Commands;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.Utilities.SendService;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Playtime implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Worldmc.getInstance().getConfig().getBoolean("Playtime.Enabled")) {
            return true;
        }
        if (sender instanceof Player) {
            Player calculatedPlayer = (Player) sender;
            HashMap<String, String> placeholders = new HashMap<>();

            if (args.length > 0) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer != null) {
                    calculatedPlayer = targetPlayer;
                } else {
                    SendService.sendMessage(calculatedPlayer, Worldmc.getInstance().getConfig().getString("Playtime.Messages.NoTarget"));
                    return true;
                }
            }

            int ticks = calculatedPlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
            int minute = 0, hour = 0;

            while (ticks > 72000) {
                ++hour;
                ticks -= 72000;
            }
            while (ticks > 1200) {
                ++minute;
                ticks -= 1200;
            }

            placeholders.put("hour", hour + "h");
            placeholders.put("minute", minute + "m");
            placeholders.put("target", calculatedPlayer.getName());

            if (sender == calculatedPlayer) {
                SendService.sendMessage((Player) sender, Worldmc.getInstance().getConfig().getString("Playtime.Messages.Self"), placeholders);
            } else {
                SendService.sendMessage((Player) sender, Worldmc.getInstance().getConfig().getString("Playtime.Messages.Other"), placeholders);
            }
        }
        return true;
    }
}
