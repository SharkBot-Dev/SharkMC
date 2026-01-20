package com.royumana.webDashboardPlugin.server.routes.modules;

import com.royumana.webDashboardPlugin.WebDashboardPlugin;
import com.royumana.webDashboardPlugin.lib.ModuleManager;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class toggle extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");
        }

        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NanoHTTPD.ResponseException e) {
            throw new RuntimeException(e);
        }
        String body = files.get("postData");
        if (body == null) return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Body is null");

        JSONObject json = new JSONObject(body);
        String moduleName = json.getString("module");

        WebDashboardPlugin plugin = (WebDashboardPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");
        ModuleManager moduleManager = plugin.getModuleManager();

        boolean isEnabled = moduleManager.isEnabled(moduleName);
        moduleManager.setEnabled(moduleName, !isEnabled);

        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "OK");
    }
}
