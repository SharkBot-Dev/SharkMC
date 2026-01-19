package com.royumana.webDashboardPlugin;

import com.royumana.webDashboardPlugin.commands.commands_registers;
import com.royumana.webDashboardPlugin.events.events_registers;
import com.royumana.webDashboardPlugin.lib.Database;
import com.royumana.webDashboardPlugin.lib.EconomyManager;
import com.royumana.webDashboardPlugin.server.routes.chat.chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.royumana.webDashboardPlugin.server.server;

import java.io.IOException;
import java.sql.SQLException;

public final class WebDashboardPlugin extends JavaPlugin {

    private static WebDashboardPlugin instance;
    private server webserver;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        saveDefaultConfig();

        Database db = new Database();
        this.economyManager = new EconomyManager(db);

        try {
            new commands_registers();
            new events_registers(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        webserver = new server(this);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            try {
                this.webserver.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            getLogger().info("Dashboard started on 0.0.0.0:8080");
        }, 60L);
    }

    @Override
    public void onDisable() {
        if (webserver != null) webserver.stop();
    }

    public static WebDashboardPlugin getInstance() {
        return instance;
    }

    public server getHttpServer() {
        return webserver;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}
