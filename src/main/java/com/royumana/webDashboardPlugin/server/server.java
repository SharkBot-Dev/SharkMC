package com.royumana.webDashboardPlugin.server;

import com.royumana.webDashboardPlugin.lib.SidebarManager;
import com.royumana.webDashboardPlugin.server.routes.chat.chat;
import com.royumana.webDashboardPlugin.server.routes.chat.getChatLogs;
import com.royumana.webDashboardPlugin.server.routes.chat.send;
import com.royumana.webDashboardPlugin.server.routes.economy.createPlayer;
import com.royumana.webDashboardPlugin.server.routes.economy.economy;
import com.royumana.webDashboardPlugin.server.routes.economy.editBalance;
import com.royumana.webDashboardPlugin.server.routes.economy.getEconomyPlayers;
import com.royumana.webDashboardPlugin.server.routes.error404;
import com.royumana.webDashboardPlugin.server.routes.index;

import com.royumana.webDashboardPlugin.server.routes.login;
import com.royumana.webDashboardPlugin.server.routes.players.*;
import com.royumana.webDashboardPlugin.server.routes.sidebar;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

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
        addRoute("/api/offlinePlayers", offlinePlayers.class);
        addRoute("/api/players", getPlayers.class);
        addRoute("/api/player/kill", kill.class, plugin);
        addRoute("/api/kick", kick.class, plugin);
        addRoute("/api/thunder", thunder.class, plugin);
        addRoute("/chat", chat.class);
        addRoute("/api/logs/chat", getChatLogs.class);
        addRoute("/api/logs/chat/send", send.class);
        addRoute("/economy", economy.class);
        addRoute("/api/economy/players", getEconomyPlayers.class);
        addRoute("/api/economy/createPlayer", createPlayer.class);
        addRoute("/api/economy/editBalance", editBalance.class);
        addRoute("/sidebar.html", sidebar.class);

        List<Map<?, ?>> customRoutes = plugin.getConfig().getMapList("routes");

        for (Map<?, ?> entry : customRoutes) {
            String path = (String) entry.get("path");
            String className = (String) entry.get("class");

            try {
                Class<?> clazz = Class.forName(className);

                addRoute(path, (Class<?>) clazz);

                plugin.getLogger().info("Route registered: " + path + " -> " + className);
            } catch (ClassNotFoundException e) {
                plugin.getLogger().severe("Could not find route class: " + className);
            } catch (Exception e) {
                plugin.getLogger().severe("Error registering route " + path + ": " + e.getMessage());
            }
        }

        setNotFoundHandler(error404.class);
    }

    public void addRouteExternal(String url, Class<?> handler) {
        addRoute(url, handler);
        plugin.getLogger().info("Route registered: " + url);
    }
}