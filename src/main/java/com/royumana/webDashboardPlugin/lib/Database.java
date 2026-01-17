package com.royumana.webDashboardPlugin.lib;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private final File dataFile;

    public Database() {
        this.dataFile = new File("web_dashboard", "database.db");
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