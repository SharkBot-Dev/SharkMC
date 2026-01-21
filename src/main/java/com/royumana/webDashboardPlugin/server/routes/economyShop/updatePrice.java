package com.royumana.webDashboardPlugin.server.routes.economyShop;

import com.royumana.webDashboardPlugin.lib.Database;
import com.royumana.webDashboardPlugin.lib.ShopRepository;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class updatePrice extends RouterNanoHTTPD.GeneralHandler {

    private final ShopRepository shopRepository;

    public updatePrice() {
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
            int newPrice = json.getInt("price");

            Map<String, Object> shopData = shopRepository.getShopById(id);
            if (shopData == null) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Shop not found");
            }

            shopRepository.updatePrice(id, newPrice);

            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("WebDashboardPlugin"), () -> {
                World world = Bukkit.getWorld((String) shopData.get("world"));
                if (world != null) {
                    int x = (int) shopData.get("x");
                    int y = (int) shopData.get("y");
                    int z = (int) shopData.get("z");

                    Block signBlock = new Location(world, x, y + 1, z).getBlock();

                    if (signBlock.getState() instanceof Sign sign) {
                        sign.setLine(2, "Price: " + newPrice);
                        sign.update();
                    }
                }
            });

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "Price updated");

        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Error updating price");
        }
    }
}