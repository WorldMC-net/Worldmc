package net.worldmc.worldmc.Utilities;

import net.worldmc.worldmc.Worldmc;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashSet;

public class RemoveEntityDrops {
    public static HashSet<Material> items = new HashSet<>();

    public static void removeEntityDrops(EntityDeathEvent event) {
        if (!Worldmc.getInstance().getConfig().getBoolean("RemoveEntityDrops.Enabled")) {
            return;
        }

        for (int i = 0; i < event.getDrops().size(); ++i) {
            if (items.contains(event.getDrops().get(i).getType())) {
                event.getDrops().remove(i);
                --i;
            }
        }
    }
}
