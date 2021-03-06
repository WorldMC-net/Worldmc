package net.worldmc.worldmc.Commands;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.Utilities.RandomTeleport;
import net.worldmc.worldmc.Utilities.SendService;
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

            if (disabledWorlds.contains(player.getWorld()) || player.getStatistic(Statistic.PLAY_ONE_MINUTE) > Worldmc.getInstance().getConfig().getInt("Wild.Playtime") && !player.isOp()) {
                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Restricted"));
                return true;
            }

            if (onCooldown.containsKey(player) && !player.isOp()) {
                long timeElapsed = (onCooldown.get(player) + Worldmc.getInstance().getConfig().getInt("Wild.Cooldown")) - Instant.now().getEpochSecond();

                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("second", Long.toString(timeElapsed));

                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Cooldown"), placeholders);
                return true;
            }

            onCooldown.put(player, Instant.now().getEpochSecond());

            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Finding"));

            RandomTeleport.issueTeleport(player);

            Bukkit.getScheduler().runTaskLater(Worldmc.getInstance(), () -> onCooldown.remove(player), Worldmc.getInstance().getConfig().getInt("Wild.Cooldown") * 20L);
        }
        return true;
    }
}
