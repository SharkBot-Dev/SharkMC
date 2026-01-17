package com.royumana.webDashboardPlugin.commands;

import com.royumana.webDashboardPlugin.commands.tools.afk;
import com.royumana.webDashboardPlugin.lib.Database;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getLogger;

public class commands_registers {
    public commands_registers() throws SQLException {
        Database connection = new Database();
        new afk(connection);

        getLogger().info("コマンドを登録しました。");
    }
}
