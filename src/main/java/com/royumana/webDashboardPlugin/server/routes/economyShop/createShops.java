package com.royumana.webDashboardPlugin.server.routes.economyShop;

import com.royumana.webDashboardPlugin.lib.Database;
import com.royumana.webDashboardPlugin.lib.ShopRepository;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.royumana.webDashboardPlugin.server.isAuth.isLogined;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class createShops extends RouterNanoHTTPD.GeneralHandler {

    private final ShopRepository shopRepository;

    public createShops() {
        this.shopRepository = new ShopRepository(new Database());
        this.shopRepository.createTable();
    }

    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isLogined(session)) return newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");

        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            String body = files.get("postData");
            if (body == null) return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Body is null");

            JSONObject json = new JSONObject(body);

            String shopName = json.getString("shopName");
            int price = json.getInt("price");
            String itemName = json.getString("itemName");
            String worldName = json.getString("world");
            int x = json.getInt("x");
            int y = json.getInt("y");
            int z = json.getInt("z");
            String ownerName = json.getString("ownerName");

            try {
                Material material = Material.matchMaterial(itemName.toUpperCase());
                if (material == null) {
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Invalid Material Name");
                }

                shopRepository.saveShopFromJson(body);

                Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("WebDashboardPlugin"), () -> {
                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        Location loc = new Location(world, x, y, z);
                        ItemStack item = new ItemStack(material);
                        createShopStructure(loc, ownerName, shopName, price, item);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Error saving to database or world");
            }

            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", "OK");

        } catch (Exception e) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Internal Server Error.");
        }
    }

    private void createShopStructure(Location loc, String ownerName, String shopName, Integer price, ItemStack item) {
        Block chestBlock = loc.getBlock();
        chestBlock.setType(Material.CHEST);

        Block signBlock = loc.clone().add(0, 1, 0).getBlock();
        signBlock.setType(Material.OAK_SIGN);

        if (signBlock.getState() instanceof Sign sign) {
            sign.setLine(0, "§b[" + shopName + "]");
            sign.setLine(1, ownerName);
            sign.setLine(2, "Price: " + price);
            sign.setLine(3, item.getType().name());
            sign.setWaxed(true);
            sign.update();
        }
    }
}