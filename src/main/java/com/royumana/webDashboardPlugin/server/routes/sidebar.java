package com.royumana.webDashboardPlugin.server.routes;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

import com.royumana.webDashboardPlugin.lib.SidebarManager;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.util.Map;

public class sidebar extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"sidebar\">");
        html.append("<h2>Dashboard</h2>");
        html.append("<div class=\"menu\">");
        html.append("<a href=\"/\">ホーム</a>");
        html.append("<a href=\"/chat\">チャット</a>");
        html.append("<a href=\"/players\">プレイヤー</a>");
        html.append("<a href=\"/economy\">経済</a>");

        for (SidebarManager.MenuEntry entry : SidebarManager.getMenus()) {
            html.append(String.format("<a href=\"%s\">%s</a>", entry.url, entry.name));
        }

        html.append("</div></div>");

        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK,
                "text/html; charset=UTF-8",
                html.toString()
        );
    };
}
