package net.worldmc.worldmc.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.worldmc.worldmc.Worldmc;
import net.worldmc.worldmc.utilities.WelcomeReward;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChat implements Listener {

    @EventHandler
    public static void asyncChatEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message()).toLowerCase();

        if (message.contains(Worldmc.getInstance().getConfig().getString("WelcomeReward.Trigger"))) {
            WelcomeReward.playerWelcome(player);
        }
    }
}
