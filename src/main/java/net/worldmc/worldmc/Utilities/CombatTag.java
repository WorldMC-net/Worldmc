package net.worldmc.worldmc.Utilities;

import net.worldmc.worldmc.Worldmc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class CombatTag {
    private static final HashMap<UUID, Long> taggedPlayers = new HashMap<>();

    public static void tagPlayer(Player player) {
        if (!getTag(player)) {
            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("CombatTag.Messages.Tagged"));
        }

        taggedPlayers.put(player.getUniqueId(), Instant.now().getEpochSecond());

        Bukkit.getScheduler().runTaskLater(Worldmc.getInstance(), () -> {
            if (Instant.now().getEpochSecond() - taggedPlayers.get(player.getUniqueId()) >= Worldmc.getInstance().getConfig().getInt("CombatTag.TagTime")) {
                taggedPlayers.remove(player.getUniqueId());
                SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("CombatTag.Messages.Untagged"));
            }
        }, Worldmc.getInstance().getConfig().getInt("CombatTag.TagTime") * 20L);
    }

    public static boolean getTag(Player player) {
        if (taggedPlayers.containsKey(player.getUniqueId())) {
            return true;
        }
        return false;
    }
}
