package com.royumana.webDashboardPlugin.commands.tools;

import com.royumana.webDashboardPlugin.lib.register_command;
import com.royumana.webDashboardPlugin.lib.register_permission;
import com.royumana.webDashboardPlugin.lib.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class afk {
    private final Database db;

    public afk(Database database) {
        this.db = database;
        initialize();

        new register_permission().registerPermission("webDashboardPlugin.tools.afk_use", "AFKコマンドを使用できます。", PermissionDefault.OP);

        new register_command().registerCommand("afk", new Command("afk") {
            {
                setDescription("AFK状態を切り替えます。");
                setUsage("/afk [理由]");
                setPermission("webDashboardPlugin.tools.afk_use");
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("プレイヤーのみ実行可能です。");
                    return true;
                }

                if (!player.hasPermission("webDashboardPlugin.tools.afk_use")) {
                    player.sendMessage("§c権限がありません。");
                    return true;
                }

                String reason = (args.length > 0) ? String.join(" ", args) : "なし";

                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("WebDashboardPlugin"), () -> {
                    toggleAFK(player, reason);
                });

                return true;
            }
        });
    }

    private void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS player_afk (" +
                "uuid TEXT PRIMARY KEY," +
                "is_afk INTEGER NOT NULL DEFAULT 0," +
                "reason TEXT" +
                ");";
        try (Connection conn = db.getConnection();
             var statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void toggleAFK(Player player, String reason) {
        String uuid = player.getUniqueId().toString();

        try (Connection conn = db.getConnection()) {
            int currentState = 0;
            String selectSql = "SELECT is_afk FROM player_afk WHERE uuid = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    currentState = rs.getInt("is_afk");
                }
            }

            int newState = (currentState == 0) ? 1 : 0;

            String replaceSql = "REPLACE INTO player_afk (uuid, is_afk, reason) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(replaceSql)) {
                ps.setString(1, uuid);
                ps.setInt(2, newState);
                ps.setString(3, newState == 1 ? reason : ""); // 解除時は理由を空にする
                ps.executeUpdate();
            }

            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("WebDashboardPlugin"), () -> {
                if (newState == 1) {
                    player.sendMessage("§7[§6AFK§7] §fAFK状態になりました。 §7(理由: " + reason + ")");
                    Bukkit.broadcastMessage("§7[§6AFK§7] §e" + player.getName() + " §fが離席しました: §7" + reason);
                } else {
                    player.sendMessage("§7[§6AFK§7] §fAFK状態を解除しました。");
                    Bukkit.broadcastMessage("§7[§6AFK§7] §e" + player.getName() + " §fが戻りました。");
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}