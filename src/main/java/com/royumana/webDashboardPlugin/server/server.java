package com.royumana.webDashboardPlugin.server;

import com.royumana.webDashboardPlugin.lib.SidebarManager;
import com.royumana.webDashboardPlugin.server.routes.chat.chat;
import com.royumana.webDashboardPlugin.server.routes.chat.getChatLogs;
import com.royumana.webDashboardPlugin.server.routes.chat.send;
import com.royumana.webDashboardPlugin.server.routes.error404;
import com.royumana.webDashboardPlugin.server.routes.index;

import com.royumana.webDashboardPlugin.server.routes.login;
import com.royumana.webDashboardPlugin.server.routes.players.*;
import com.royumana.webDashboardPlugin.server.routes.sidebar;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import static org.bukkit.Bukkit.getLogger;

public class server extends RouterNanoHTTPD {

    private final JavaPlugin plugin;

    public server(JavaPlugin plugin) {
        super("0.0.0.0", 8080);
        this.plugin = plugin;

        addMappings();
    }

    @Override
    public void addMappings() {
        addRoute("/", index.class);
        addRoute("/login", login.class);
        addRoute("/players", players.class);
        addRoute("/api/players", getPlayers.class);
        addRoute("/api/player/kill", kill.class, plugin);
        addRoute("/api/kick", kick.class, plugin);
        addRoute("/api/thunder", thunder.class, plugin);
        addRoute("/chat", chat.class);
        addRoute("/api/logs/chat", getChatLogs.class);
        addRoute("/api/logs/chat/send", send.class);
        addRoute("/sidebar.html", sidebar.class);

        setNotFoundHandler(error404.class);
    }

    public void addRouteExternal(String url, Class<?> handler) {
        addRoute(url, handler);
        getLogger().info(url + " というルートを追加しました。");
    }
}