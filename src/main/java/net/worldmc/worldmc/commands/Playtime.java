package net.worldmc.worldmc.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.utilities.SendService;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Playtime implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Worldmc.getInstance().getConfig().getBoolean("Playtime.Enabled")) {
            return true;
        }
        if (sender instanceof Player) {
            Player calculatedPlayer = (Player) sender;

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

            TagResolver.Single hourP = Placeholder.parsed("hour", hour + "h");
            TagResolver.Single minuteP = Placeholder.parsed("minute", minute + "m");
            TagResolver.Single target = Placeholder.parsed("target", calculatedPlayer.getName());
            TagResolver placeholders = TagResolver.resolver(hourP, minuteP, target);

            if (sender == calculatedPlayer) {
                SendService.sendMessage((Player) sender, Worldmc.getInstance().getConfig().getString("Playtime.Messages.Self"), placeholders);
            } else {
                SendService.sendMessage((Player) sender, Worldmc.getInstance().getConfig().getString("Playtime.Messages.Other"), placeholders);
            }
        }
        return true;
    }
}
