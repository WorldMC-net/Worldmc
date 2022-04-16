package net.worldmc.worldmc.Events;

import com.github.puregero.multilib.MultiLib;
import net.worldmc.worldmc.Utilities.CombatTag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public static void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player attacker = ((Player) event.getDamager()).getPlayer();
            Player defender = ((Player) event.getEntity()).getPlayer();

            if (MultiLib.isLocalPlayer(attacker)) {
                CombatTag.tagPlayer(attacker);
            }

            if (MultiLib.isLocalPlayer(defender)) {
                CombatTag.tagPlayer(defender);
            }
        }
    }
}
