package com.royumana.webDashboardPlugin.server;

import com.royumana.webDashboardPlugin.server.routes.error404;
import com.royumana.webDashboardPlugin.server.routes.index;

import com.royumana.webDashboardPlugin.server.routes.login;
import com.royumana.webDashboardPlugin.server.routes.players.getPlayers;
import com.royumana.webDashboardPlugin.server.routes.players.kick;
import com.royumana.webDashboardPlugin.server.routes.players.players;
import com.royumana.webDashboardPlugin.server.routes.players.thunder;
import com.royumana.webDashboardPlugin.server.routes.sidebar;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.plugin.java.JavaPlugin;

public class server extends RouterNanoHTTPD {

    private final JavaPlugin plugin;

    public server(JavaPlugin plugin) {
        super("127.0.0.1", 8080);
        this.plugin = plugin;

        addMappings();
    }

    @Override
    public void addMappings() {
        addRoute("/", index.class);
        addRoute("/login", login.class);
        addRoute("/players", players.class);
        addRoute("/api/players", getPlayers.class);
        addRoute("/api/kick", kick.class, plugin);
        addRoute("/api/thunder", thunder.class, plugin);
        addRoute("/sidebar.html", sidebar.class);

        setNotFoundHandler(error404.class);
    }
}