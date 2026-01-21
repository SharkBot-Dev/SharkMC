package com.royumana.webDashboardPlugin.server.routes.economyShop;

import com.royumana.webDashboardPlugin.lib.Database;
import com.royumana.webDashboardPlugin.lib.ShopRepository;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class deleteShop extends RouterNanoHTTPD.GeneralHandler {

    private final ShopRepository shopRepository;

    public deleteShop() {
        this.shopRepository = new ShopRepository(new Database());
    }

    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");

        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            String body = files.get("postData");
            JSONObject json = new JSONObject(body);
            int id = json.getInt("id");

            Map<String, Object> shopData = shopRepository.getShopById(id);
            if (shopData == null) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Shop not found");
            }

            shopRepository.deleteShop(id);

            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("WebDashboardPlugin"), () -> {
                World world = Bukkit.getWorld((String) shopData.get("world"));
                if (world != null) {
                    int x = (int) shopData.get("x");
                    int y = (int) shopData.get("y");
                    int z = (int) shopData.get("z");

                    new Location(world, x, y, z).getBlock().setType(Material.AIR);
                    new Location(world, x, y + 1, z).getBlock().setType(Material.AIR);
                }
            });

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "Shop deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Error deleting shop");
        }
    }
}