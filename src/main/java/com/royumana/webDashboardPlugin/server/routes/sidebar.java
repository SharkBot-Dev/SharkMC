package com.royumana.webDashboardPlugin.server.routes;

import com.royumana.webDashboardPlugin.server.files;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.util.Map;

public class sidebar extends RouterNanoHTTPD.GeneralHandler {
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        var files_class = new files();

        return files_class.html(
                "web/sidebar.html"
        );
    };
}
