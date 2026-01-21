package com.royumana.webDashboardPlugin.lib;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopRepository {
    private final Database database;

    public ShopRepository(Database database) {
        this.database = database;
        createTable();
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS shops (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "shop_name TEXT," +
                "price INTEGER," +
                "item_name TEXT," +
                "world_name TEXT," +
                "x INTEGER," +
                "y INTEGER," +
                "z INTEGER," +
                "owner_name TEXT" +
                ");";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveShopFromJson(String body) {
        JSONObject json = new JSONObject(body);

        String sql = "INSERT INTO shops (shop_name, price, item_name, world_name, x, y, z, owner_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, json.getString("shopName"));
            pstmt.setInt(2, json.getInt("price"));
            pstmt.setString(3, json.getString("itemName"));
            pstmt.setString(4, json.getString("world"));
            pstmt.setInt(5, json.getInt("x"));
            pstmt.setInt(6, json.has("y") ? json.getInt("y") : 0);
            pstmt.setInt(7, json.getInt("z"));
            pstmt.setString(8, json.getString("ownerName"));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getAllShops() {
        List<Map<String, Object>> shops = new ArrayList<>();
        String sql = "SELECT * FROM shops";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> shop = new HashMap<>();
                shop.put("id", rs.getInt("id"));
                shop.put("shopName", rs.getString("shop_name"));
                shop.put("price", rs.getInt("price"));
                shop.put("itemName", rs.getString("item_name"));
                shop.put("world", rs.getString("world_name"));
                shop.put("x", rs.getInt("x"));
                shop.put("y", rs.getInt("y"));
                shop.put("z", rs.getInt("z"));
                shop.put("ownerName", rs.getString("owner_name"));
                shops.add(shop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shops;
    }

    public Map<String, Object> getShopById(int id) {
        String sql = "SELECT * FROM shops WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> shop = new HashMap<>();
                    shop.put("world", rs.getString("world_name"));
                    shop.put("x", rs.getInt("x"));
                    shop.put("y", rs.getInt("y"));
                    shop.put("z", rs.getInt("z"));
                    return shop;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteShop(int id) {
        String sql = "DELETE FROM shops WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePrice(int id, int newPrice) {
        String sql = "UPDATE shops SET price = ? WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newPrice);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getShopByLocation(String world, int x, int y, int z) {
        String sql = "SELECT * FROM shops WHERE world_name = ? AND x = ? AND y = ? AND z = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, world);
            pstmt.setInt(2, x);
            pstmt.setInt(3, y);
            pstmt.setInt(4, z);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> shop = new HashMap<>();
                    shop.put("id", rs.getInt("id"));
                    shop.put("price", rs.getInt("price"));
                    shop.put("itemName", rs.getString("item_name"));
                    shop.put("ownerName", rs.getString("owner_name"));
                    return shop;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isShopBlock(String world, int x, int y, int z) {
        String sql = "SELECT id FROM shops WHERE world_name = ? AND x = ? AND z = ? AND (y = ? OR y = ?)";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, world);
            pstmt.setInt(2, x);
            pstmt.setInt(3, z);
            pstmt.setInt(4, y);
            pstmt.setInt(5, y - 1);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // データがあればtrue
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}