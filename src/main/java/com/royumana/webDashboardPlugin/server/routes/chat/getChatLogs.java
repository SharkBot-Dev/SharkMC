package com.royumana.webDashboardPlugin.server.routes.chat;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.util.List;
import java.util.Map;

import com.royumana.webDashboardPlugin.events.onPlayerChat.ChatLogManager;
import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class getChatLogs extends RouterNanoHTTPD.GeneralHandler  {
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");

        List<String> logs = ChatLogManager.getLogs();

        // 簡易的にJSON配列を作成 (本来はGson等を使うのがベスト)
        String json = "[\"" + String.join("\",\"", logs) + "\"]";

        NanoHTTPD.Response res = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", json);
        // CORS対策（ブラウザからの直接アクセスを許可する場合）
        res.addHeader("Access-Control-Allow-Origin", "*");
        return res;
    };
}
