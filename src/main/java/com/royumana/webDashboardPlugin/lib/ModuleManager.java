package com.royumana.webDashboardPlugin.lib;

import java.sql.*;

public class ModuleManager {

    private final Database database;

    public ModuleManager(Database database) {
        this.database = database;
        initTable();
    }

    private void initTable() {
        try (Connection con = database.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS modules (
                    name TEXT PRIMARY KEY,
                    enabled INTEGER NOT NULL
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setEnabled(String name, boolean enabled) {
        String sql = "INSERT OR REPLACE INTO modules (name, enabled) VALUES (?, ?)";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, enabled ? 1 : 0);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnabled(String name) {
        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT enabled FROM modules WHERE name = ?"
             )) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("enabled") == 1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
