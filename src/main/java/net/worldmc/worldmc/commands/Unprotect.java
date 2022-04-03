package net.worldmc.worldmc.commands;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.database.MySQL;
import net.worldmc.worldmc.utilities.NewPlayerProtection;
import net.worldmc.worldmc.utilities.SendService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unprotect implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Worldmc.getInstance().getConfig().getBoolean("Unprotect.Enabled")) {
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (MySQL.getProtection(player.getUniqueId())) {
                NewPlayerProtection.disableProtection(player);
                return true;
            }

            SendService.sendMessage(player, Worldmc.getInstance().getConfig().getString("Unprotect.Message"));
        }
        return true;
    }
}
