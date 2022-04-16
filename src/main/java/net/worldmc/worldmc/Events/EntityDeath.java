package net.worldmc.worldmc.Events;

import net.worldmc.worldmc.Utilities.RemoveEntityDrops;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {

    @EventHandler
    public void entityDeathEvent(EntityDeathEvent event) {
        RemoveEntityDrops.removeEntityDrops(event);
    }
}
