package com.royumana.webDashboardPlugin.server.routes.economy;

import com.royumana.webDashboardPlugin.WebDashboardPlugin;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class getEconomyPlayers extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");
        }

        WebDashboardPlugin plugin = (WebDashboardPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");
        Map<UUID, Double> economyData = plugin.getEconomyManager().getAllEconomyPlayers();

        String json = economyData.entrySet().stream()
                .map(entry -> {
                    UUID uuid = entry.getKey();
                    double balance = entry.getValue();
                    OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
                    String name = (op.getName() != null) ? op.getName() : "Unknown";

                    return String.format("{\"name\":\"%s\",\"uuid\":\"%s\",\"balance\":%.2f}",
                            name, uuid.toString(), balance);
                })
                .collect(Collectors.joining(",", "[", "]"));

        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK,
                "application/json",
                json
        );
    }
}