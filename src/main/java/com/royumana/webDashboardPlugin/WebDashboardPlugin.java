package com.royumana.webDashboardPlugin;

import com.royumana.webDashboardPlugin.commands.commands_registers;
import fi.iki.elonen.NanoHTTPD;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.royumana.webDashboardPlugin.server.server;

import java.io.IOException;
import java.sql.SQLException;

public final class WebDashboardPlugin extends JavaPlugin {

    private server webserver;

    @Override
    public void onEnable() {
        try {
            new commands_registers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        webserver = new server(this);
        try {
            webserver.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            getLogger().info("Dashboard started on 127.0.0.1:8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (webserver != null) webserver.stop();
    }
}
