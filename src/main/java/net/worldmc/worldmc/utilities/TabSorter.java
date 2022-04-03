package net.worldmc.worldmc.utilities;

import net.worldmc.worldmc.Worldmc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Scoreboard;

public class TabSorter {

    public static void setTab() {
        Bukkit.getScheduler().runTaskLater(Worldmc.getInstance(), () -> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

            for (Player player : Bukkit.getOnlinePlayers()) {
                for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
                    String permission = permissionAttachmentInfo.getPermission();
                    if (!permission.startsWith("tab.")) {
                        continue;
                    }
                    if (scoreboard.getTeam(permission.substring(4)) == null) {
                        scoreboard.registerNewTeam(permission.substring(4));
                    }
                    scoreboard.getTeam(permission.substring(4)).addEntry(player.getName());
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setScoreboard(scoreboard);
            }
        }, 1L);
    }
}
