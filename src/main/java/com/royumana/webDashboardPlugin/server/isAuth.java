package com.royumana.webDashboardPlugin.server;

import fi.iki.elonen.NanoHTTPD;

import java.util.HashMap;
import java.util.Map;

public class isAuth {
    public static final Map<String, String> sessionStore = new HashMap<>();

    public static boolean isLogined(NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.CookieHandler cookies = session.getCookies();
        String sessionId = cookies.read("SESSIONID");
        return sessionId != null && sessionStore.containsKey(sessionId);
    }
}
