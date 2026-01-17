package com.royumana.webDashboardPlugin.server.routes.players;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class thunder extends RouterNanoHTTPD.GeneralHandler {

    public thunder() {
        super();
    }

    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");

        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
        } catch (Exception e) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", e.getMessage());
        }

        String body = files.get("postData");
        if (body == null) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "body is null");
        }

        JSONObject json = new JSONObject(body);
        String playerName = json.getString("player");

        JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");

        if (plugin != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                Player p = Bukkit.getPlayerExact(playerName);
                if (p != null) {
                    p.getWorld().strikeLightning(p.getLocation());
                }
            });
        }

        return newFixedLengthResponse("OK");
    }
}