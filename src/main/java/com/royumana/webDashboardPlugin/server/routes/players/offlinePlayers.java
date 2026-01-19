package com.royumana.webDashboardPlugin.server.routes.players;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class offlinePlayers extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");
        }

        OfflinePlayer[] allOfflinePlayers = Bukkit.getOfflinePlayers();

        String json = Arrays.stream(allOfflinePlayers)
                .map(OfflinePlayer::getName)     // 名前を取得
                .filter(Objects::nonNull)        // 名前がnullのデータ（稀に存在）を除外
                .map(name -> "\"" + name + "\"") // ダブルクォーテーションで囲む
                .collect(Collectors.joining(",", "[", "]")); // カンマ区切りで[]に入れる

        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK,
                "application/json",
                json
        );
    }
}