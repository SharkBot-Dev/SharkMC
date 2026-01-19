package com.royumana.webDashboardPlugin.lib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private final File dataFile;

    public Database() {
        JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");
        this.dataFile = new File(plugin.getDataFolder(), "database.db");
    }

    public Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        if (!dataFile.getParentFile().exists()) {
            dataFile.getParentFile().mkdirs();
        }

        String url = "jdbc:sqlite:" + dataFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
        return connection;
    }
}