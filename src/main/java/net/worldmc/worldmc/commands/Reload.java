package net.worldmc.worldmc.commands;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.utilities.SendService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            Worldmc.getInstance().reloadConfig();
            SendService.sendMessage((Player) sender, Worldmc.getInstance().getConfig().getString("Reload.Message"));
        } else {
            SendService.sendMessage((Player) sender, Worldmc.getInstance().getConfig().getString("NoPermission"));
        }
        return true;
    }
}
