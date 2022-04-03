package net.worldmc.worldmc.utilities;

import net.worldmc.worldmc.Worldmc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;

public class CombatTag {
    public static final HashMap<Player, Long> taggedPlayers = new HashMap<>();

    public static void tagPlayer(Player player) {
        if (!taggedPlayers.containsKey(player)) {
            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("CombatTag.Messages.Tagged"));
        }

        taggedPlayers.put(player, Instant.now().getEpochSecond());

        Bukkit.getScheduler().runTaskLater(Worldmc.getInstance(), () -> {
            if (Instant.now().getEpochSecond() - taggedPlayers.get(player) >= Worldmc.getInstance().getConfig().getInt("CombatTag.TagTime")) {
                taggedPlayers.remove(player);
                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("CombatTag.Messages.Untagged"));
            }
        }, Worldmc.getInstance().getConfig().getInt("CombatTag.TagTime") * 20L);

    }
}