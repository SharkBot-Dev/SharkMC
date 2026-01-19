package com.royumana.webDashboardPlugin.server.routes.economy;

import com.royumana.webDashboardPlugin.WebDashboardPlugin;
import com.royumana.webDashboardPlugin.lib.EconomyManager;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class editBalance extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");

        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            String body = files.get("postData");
            if (body == null) return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Body is null");

            JSONObject json = new JSONObject(body);
            String playerName = json.getString("player");
            double amount = json.getDouble("amount");

            WebDashboardPlugin plugin = (WebDashboardPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");
            EconomyManager ecm = plugin.getEconomyManager();

            @SuppressWarnings("deprecation")
            OfflinePlayer op = Bukkit.getOfflinePlayer(playerName);

            if (op == null || !op.hasPlayedBefore()) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Player not found");
            }

            ecm.setBalance(op.getUniqueId(), amount);

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "Updated " + playerName + "'s balance to " + amount);

        } catch (Exception e) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", e.getMessage());
        }
    }
}