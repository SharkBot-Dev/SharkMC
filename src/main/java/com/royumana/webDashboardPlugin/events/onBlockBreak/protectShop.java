package com.royumana.webDashboardPlugin.events.onBlockBreak;

import com.royumana.webDashboardPlugin.lib.Database;
import com.royumana.webDashboardPlugin.lib.ShopRepository;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class protectShop implements Listener {

    private final ShopRepository shopRepository;

    public protectShop() {
        this.shopRepository = new ShopRepository(new Database());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        String world = loc.getWorld().getName();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        if (shopRepository.isShopBlock(world, x, y, z) ||
                shopRepository.isShopBlock(world, x, y - 1, z)) {

            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cこのショップは保護されています。削除はWebダッシュボードから行ってください。");
            }
        }
    }
}