package com.royumana.webDashboardPlugin.server.routes.chat;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.security.cert.Extension;
import java.util.Map;

import com.royumana.webDashboardPlugin.events.onPlayerChat.ChatLogManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class send extends RouterNanoHTTPD.GeneralHandler  {
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");

        try {
            java.util.Map<String, String> files = new java.util.HashMap<>();
            session.parseBody(files);

            String message = files.get("postData");

            if (message != null && !message.isEmpty()) {
                JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");
                if (message.startsWith("/")) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        try {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message.split("/")[1]);
                        } catch (Exception ignored) {}
                    });
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "OK");
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Bukkit.broadcastMessage("§b[Web管理画面] §f" + message);
                });
                String entry = String.format("[%s] %s: %s",
                        new java.util.Date().toString(),
                        "Web管理画面",
                        message);
                ChatLogManager.addLogs(entry);
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "OK");
            }
        } catch (Exception e) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", e.getMessage());
        };

        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Not Found");
    };
}