package com.royumana.webDashboardPlugin.server.routes.economyShop;

import com.royumana.webDashboardPlugin.server.files;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.util.Map;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class shop extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) {
            NanoHTTPD.Response res = newFixedLengthResponse(NanoHTTPD.Response.Status.REDIRECT, "text/plain", "");
            res.addHeader("Location", "/login");
            return res;
        }

        var files_class = new files();

        return files_class.html(
                "web/shop/index.html"
        );
    };
}
