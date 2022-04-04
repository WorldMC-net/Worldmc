package net.worldmc.worldmc.commands;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.utilities.RandomTeleport;
import net.worldmc.worldmc.utilities.SendService;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;

public class Wild implements CommandExecutor {
    public static HashSet<World> disabledWorlds = new HashSet<>();
    private static final HashMap<Player, Long> onCooldown = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (disabledWorlds.contains(player.getWorld()) || player.getStatistic(Statistic.PLAY_ONE_MINUTE) > 72000 && !player.isOp()) {
                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Restricted"));
                return true;
            }

            if (onCooldown.containsKey(player)) {
                long timeElapsed = (onCooldown.get(player) + Worldmc.getInstance().getConfig().getInt("Wild.Cooldown")) - Instant.now().getEpochSecond();

                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("second", Long.toString(timeElapsed));

                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Cooldown"), placeholders);
                return true;
            }

            onCooldown.put(player, Instant.now().getEpochSecond());

            RandomTeleport.randomTeleport(player, Bukkit.getWorld("world"));

            Bukkit.getScheduler().runTaskLater(Worldmc.getInstance(), () -> onCooldown.remove(player), Worldmc.getInstance().getConfig().getInt("Wild.Cooldown") * 20L);
        }
        return true;
    }
}
