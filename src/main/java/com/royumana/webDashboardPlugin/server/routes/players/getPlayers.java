package com.royumana.webDashboardPlugin.server.routes.players;

import com.royumana.webDashboardPlugin.server.isAuth;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.stream.Collectors;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class getPlayers extends RouterNanoHTTPD.GeneralHandler  {
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");

        String json = Bukkit.getOnlinePlayers().stream()
                .map(p -> "\"" + p.getName() + "\"")
                .collect(Collectors.joining(",", "[", "]"));

        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK,
                "application/json",
                json
        );
    };
}
