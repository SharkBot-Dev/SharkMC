package com.royumana.webDashboardPlugin.server.routes;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

public class error404 extends RouterNanoHTTPD.DefaultHandler {
    @Override
    public String getText() {
        return "<html><body><h3>404 Not Found</h3></body></html>";
    }
    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.NOT_FOUND;
    }
    @Override
    public String getMimeType() {
        return "text/html";
    }
}