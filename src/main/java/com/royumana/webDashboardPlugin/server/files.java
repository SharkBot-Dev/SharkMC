package com.royumana.webDashboardPlugin.server;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class files {

    private String loadResource(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) return "404";
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Error";
        }
    }

    public NanoHTTPD.Response html(String path) {
        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK,
                "text/html; charset=UTF-8",
                loadResource(path)
        );
    }

    public NanoHTTPD.Response css(String path) {
        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK,
                "text/css; charset=UTF-8",
                loadResource(path)
        );
    }

    public NanoHTTPD.Response js(String path) {
        return newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK,
                "application/javascript; charset=UTF-8",
                loadResource(path)
        );
    }
}
