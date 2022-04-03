package net.worldmc.worldmc.utilities;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.database.MySQL;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class NewPlayerProtection {

    public static final HashSet<Player> protectedPlayers = new HashSet<>();

    public static void checkEnabledProtection(Player player) {
        if (MySQL.getProtection(player.getUniqueId())) {
            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("NewPlayerProtection.Messages.Enabled"));
            protectedPlayers.add(player);
        }
    }

    public static void disableProtection(Player player) {
        UUID uuid = player.getUniqueId();

        if (MySQL.getProtection(player.getUniqueId())) {
            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("NewPlayerProtection.Messages.Disabled"));
            protectedPlayers.remove(player);
            MySQL.disableProtection(uuid);
        }
    }
}
