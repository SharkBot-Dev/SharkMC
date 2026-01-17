package com.royumana.webDashboardPlugin;

import fi.iki.elonen.NanoHTTPD;
import org.bukkit.plugin.java.JavaPlugin;
import com.royumana.webDashboardPlugin.server.server;

import java.io.IOException;

public final class WebDashboardPlugin extends JavaPlugin {

    private server webserver;

    @Override
    public void onEnable() {
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
