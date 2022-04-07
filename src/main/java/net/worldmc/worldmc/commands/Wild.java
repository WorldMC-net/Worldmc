package net.worldmc.worldmc.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.database.MySQL;
import net.worldmc.worldmc.utilities.RandomTeleport;
import net.worldmc.worldmc.utilities.SendService;
import org.bukkit.Bukkit;
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

            if (disabledWorlds.contains(player.getWorld()) || !MySQL.getProtection(player.getUniqueId()) && !player.isOp()) {
                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Restricted"));
                return true;
            }

            if (onCooldown.containsKey(player)) {
                long timeElapsed = (onCooldown.get(player) + Worldmc.getInstance().getConfig().getInt("Wild.Cooldown")) - Instant.now().getEpochSecond();

                TagResolver.Single second = Placeholder.parsed("second", Long.toString(timeElapsed));
                TagResolver placeholders = TagResolver.resolver(second);

                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Wild.Messages.Cooldown"), placeholders);
                return true;
            }

            onCooldown.put(player, Instant.now().getEpochSecond());

            RandomTeleport.createRTP(player, Bukkit.getWorld("world"));

            Bukkit.getScheduler().runTaskLater(Worldmc.getInstance(), () -> onCooldown.remove(player), Worldmc.getInstance().getConfig().getInt("Wild.Cooldown") * 20L);
        }
        return true;
    }
}
