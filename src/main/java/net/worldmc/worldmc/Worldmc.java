package net.worldmc.worldmc;

import net.worldmc.worldmc.Commands.Broadcast;
import net.worldmc.worldmc.Commands.Playtime;
import net.worldmc.worldmc.Commands.Reload;
import net.worldmc.worldmc.Commands.Wild;
import net.worldmc.worldmc.Database.MySQL;
import net.worldmc.worldmc.Events.*;
import net.worldmc.worldmc.Utilities.RandomTeleport;
import net.worldmc.worldmc.Utilities.RemoveEntityDrops;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

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
            Bukkit.getPluginManager().disablePlugin(worldmc);
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

        getCommand("wmc-reload").setExecutor(new Reload());
        getCommand("broadcast").setExecutor(new Broadcast());

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new AsyncChat(), this);
        getServer().getPluginManager().registerEvents(new EntityDeath(), this);
        getServer().getPluginManager().registerEvents(new Votifier(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        for (String world : getConfig().getStringList("Wild.DisabledWorlds")) {
            Wild.disabledWorlds.add(Bukkit.getWorld(world));
        }

        for (String item : getConfig().getStringList("RemoveEntityDrops.Items")) {
            RemoveEntityDrops.items.add(Material.matchMaterial(item));
        }

        for (String block : getConfig().getStringList("RandomTeleport.SafeBlocks")) {
            RandomTeleport.safeBlocks.add(Material.matchMaterial(block));
        }
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
