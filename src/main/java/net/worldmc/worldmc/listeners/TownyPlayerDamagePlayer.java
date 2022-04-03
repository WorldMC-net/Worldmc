package net.worldmc.worldmc.listeners;

import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent;
import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.database.MySQL;
import net.worldmc.worldmc.utilities.CombatTag;
import net.worldmc.worldmc.utilities.NewPlayerProtection;
import net.worldmc.worldmc.utilities.SendService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyPlayerDamagePlayer implements Listener {

    @EventHandler
    public void townyPlayerDamagePlayerEvent(TownyPlayerDamagePlayerEvent event) {
        Player victimPlayer = event.getVictimPlayer();
        Player attackingPlayer = event.getAttackingPlayer();

        if (MySQL.getProtection(attackingPlayer.getUniqueId())) {
            event.setCancelled(true);
            SendService.sendMessage(attackingPlayer, Worldmc.getInstance().getConfig().getString("NewPlayerProtection.Messages.AttackerProtected"));
            return;
        }

        if (MySQL.getProtection(victimPlayer.getUniqueId())) {
            event.setCancelled(true);
            SendService.sendMessage(attackingPlayer, Worldmc.getInstance().getConfig().getString("NewPlayerProtection.Messages.VictimProtected"));
            return;
        }

        CombatTag.tagPlayer(attackingPlayer);
        CombatTag.tagPlayer(victimPlayer);
    }
}
