package com.royumana.webDashboardPlugin.server.routes.modules;

import com.royumana.webDashboardPlugin.WebDashboardPlugin;
import com.royumana.webDashboardPlugin.lib.ModuleManager;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Level;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class enabled extends RouterNanoHTTPD.GeneralHandler {

    // プラグインのインスタンスをキャッシュするためのフィールド
    private final WebDashboardPlugin plugin;

    public enabled() {
        this.plugin = (WebDashboardPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");
    }

    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "application/json", "{\"error\": \"Unauthorized\"}");
        }

        if (plugin == null || !plugin.isEnabled()) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json", "{\"error\": \"Plugin not available\"}");
        }

        try {
            Map<String, String> params = session.getParms();
            String moduleName = params.get("name");

            if (moduleName == null || moduleName.trim().isEmpty()) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "application/json", "{\"error\": \"Module name is missing\"}");
            }

            ModuleManager moduleManager = plugin.getModuleManager();

            boolean isEnabled = moduleManager.isEnabled(moduleName);

            JSONObject responseJson = new JSONObject();
            responseJson.put("module", moduleName);
            responseJson.put("enabled", isEnabled);
            responseJson.put("status", "success");

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", responseJson.toString());

        } catch (Exception e) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json", "{\"error\": \"Internal server error\"}");
        }
    }
}