package com.royumana.webDashboardPlugin.lib;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {
    private final Database db;

    public EconomyManager(Database db) {
        this.db = db;
        initialize();
    }

    public void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS player_economy (" +
                "uuid TEXT PRIMARY KEY, " +
                "balance REAL DEFAULT 0" +
                ");";
        try (Connection conn = db.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(UUID uuid) {
        String sql = "SELECT balance FROM player_economy WHERE uuid = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // データがない場合は0
    }

    public void setBalance(UUID uuid, double amount) {
        String sql = "INSERT INTO player_economy(uuid, balance) VALUES(?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET balance = excluded.balance";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setDouble(2, amount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMoney(UUID uuid, double amount) {
        double current = getBalance(uuid);
        setBalance(uuid, current + amount);
    }

    public Map<UUID, Double> getAllEconomyPlayers() {
        String sql = "SELECT uuid, balance FROM player_economy";
        Map<UUID, Double> players = new HashMap<>();

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                double balance = rs.getDouble("balance");
                players.put(uuid, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
}