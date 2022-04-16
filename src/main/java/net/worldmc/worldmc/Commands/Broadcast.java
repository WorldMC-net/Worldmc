package net.worldmc.worldmc.Commands;

import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.Utilities.SendService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Broadcast implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : args) {
                    if (string.equals(args[0])) {
                        stringBuilder.append(string);
                    } else {
                        stringBuilder.append(" ").append(string);
                    }
                }
                SendService.sendBroadcast("<green>" + stringBuilder);
            } else {
                SendService.sendMessage((Player) sender, Worldmc.getInstance().getConfig().getString("Broadcast.Message"));
            }
        } else {
            SendService.sendMessage((Player) sender, Worldmc.getInstance().getConfig().getString("NoPermission"));
        }
        return true;
    }
}
