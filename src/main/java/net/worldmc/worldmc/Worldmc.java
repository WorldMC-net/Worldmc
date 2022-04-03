package net.worldmc.worldmc;

import net.worldmc.worldmc.commands.*;
import net.worldmc.worldmc.database.MySQL;
import net.worldmc.worldmc.listeners.*;
import net.worldmc.worldmc.utilities.NewPlayerProtection;
import net.worldmc.worldmc.utilities.RandomTeleport;
import net.worldmc.worldmc.utilities.RemoveEntityDrops;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public final class Worldmc extends JavaPlugin {
    private static Worldmc worldmc;
    public MySQL SQL;

    @Override
    public void onEnable() {
        // Plugin startup logic
        worldmc = this;
        SQL = new MySQL();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info("Database could not connect");
        }

        if (SQL.isConnected()) {
            SQL.createTables();
        }

        if (getConfig().getBoolean("Playtime.Enabled")) {
            getCommand("playtime").setExecutor(new Playtime());
        }

        if (getConfig().getBoolean("Wild.Enabled")) {
            getCommand("wild").setExecutor(new Wild());
        }

        if (getConfig().getBoolean("Unprotect.Enabled")) {
            getCommand("unprotect").setExecutor(new Unprotect());
        }

        getCommand("wmc-reload").setExecutor(new Reload());
        getCommand("broadcast").setExecutor(new Broadcast());

        getServer().getPluginManager().registerEvents(new TownyPlayerDamagePlayer(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new AsyncChat(), this);
        getServer().getPluginManager().registerEvents(new EntityDeath(), this);
        getServer().getPluginManager().registerEvents(new Votifier(), this);

        for (String world : getConfig().getStringList("Wild.DisabledWorlds")) {
            Wild.disabledWorlds.add(Bukkit.getWorld(world));
        }

        for (String item : getConfig().getStringList("RemoveEntityDrops.Items")) {
            RemoveEntityDrops.items.add(Material.matchMaterial(item));
        }

        for (String block : getConfig().getStringList("RandomTeleport.SafeBlocks")) {
            RandomTeleport.safeBlocks.add(Material.matchMaterial(block));
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : NewPlayerProtection.protectedPlayers) {
                    if (player.getStatistic(Statistic.PLAY_ONE_MINUTE) > 72000) {
                        NewPlayerProtection.disableProtection(player);
                    }
                }
            }
        }.runTaskTimer(this, 0L, 1200L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SQL.disconnect();
    }

    public static Worldmc getInstance() {
        return worldmc;
    }
}
