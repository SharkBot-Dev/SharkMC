package com.royumana.webDashboardPlugin.server.routes.economyShop;

import com.royumana.webDashboardPlugin.lib.Database;
import com.royumana.webDashboardPlugin.lib.ShopRepository;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class listShops extends RouterNanoHTTPD.GeneralHandler {

    private final ShopRepository shopRepository;

    public listShops() {
        this.shopRepository = new ShopRepository(new Database());
    }

    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");
        }

        try {
            List<Map<String, Object>> shops = shopRepository.getAllShops();

            JSONArray jsonArray = new JSONArray(shops);

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", jsonArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Internal Server Error");
        }
    }
}