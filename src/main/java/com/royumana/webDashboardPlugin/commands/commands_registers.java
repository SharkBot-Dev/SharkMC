package com.royumana.webDashboardPlugin.commands;

import com.royumana.webDashboardPlugin.commands.economy.balance;
import com.royumana.webDashboardPlugin.commands.moderation.echo;
import com.royumana.webDashboardPlugin.commands.moderation.setnick;
import com.royumana.webDashboardPlugin.commands.moderation.thunder;
import com.royumana.webDashboardPlugin.commands.tools.afk;
import com.royumana.webDashboardPlugin.lib.Database;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getLogger;

public class commands_registers {
    public commands_registers() throws SQLException {
        Database connection = new Database();
        // ツール
        new afk(connection);

        // モデレート
        new echo();
        new thunder();
        new setnick();

        // 経済
        new balance();

        getLogger().info("コマンドを登録しました。");
    }
}
