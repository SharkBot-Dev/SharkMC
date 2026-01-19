package com.royumana.webDashboardPlugin.server.routes;

import com.royumana.webDashboardPlugin.server.files;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.royumana.webDashboardPlugin.server.isAuth.sessionStore;
import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class login extends RouterNanoHTTPD.GeneralHandler {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateSessionId() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (isLogined(session)) {
            NanoHTTPD.Response res = newFixedLengthResponse(NanoHTTPD.Response.Status.REDIRECT, "text/plain", "");
            res.addHeader("Location", "/");
            return res;
        }

        var files_class = new files();
        return files_class.html("web/login.html");
    }

    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        try {
            Map<String, String> files = new HashMap<>();
            session.parseBody(files);

            Map<String, String> params = session.getParms();
            String user = params.get("username");
            String pass = params.get("password");

            String expectedUser = System.getenv("USERNAME");
            String expectedPass = System.getenv("PASSWORD");

            if (expectedUser != null && expectedPass != null &&
                    expectedUser.equals(user) && expectedPass.equals(pass)) {

                String sessionId = generateSessionId();
                sessionStore.put(sessionId, user);

                session.getCookies().set("SESSIONID", sessionId, 1);

                NanoHTTPD.Response res = newFixedLengthResponse(NanoHTTPD.Response.Status.REDIRECT, "text/plain", "");
                res.addHeader("Location", "/");
                return res;
            } else {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/html; charset=utf-8",
                        "認証失敗: ユーザー名かパスワードが違います。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Internal Error: " + e.getMessage());
        }
    }
}