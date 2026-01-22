package com.royumana.webDashboardPlugin.lib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final File dataFile;

    public Database() {
        JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");
        this.dataFile = new File(plugin.getDataFolder(), "database.db");
    }

    public Connection getConnection() throws SQLException {
        if (!dataFile.getParentFile().exists()) {
            dataFile.getParentFile().mkdirs();
        }

        String url = "jdbc:sqlite:" + dataFile.getAbsolutePath()
                   + "?busy_timeout=3000";
        return DriverManager.getConnection(url);
    }
}
