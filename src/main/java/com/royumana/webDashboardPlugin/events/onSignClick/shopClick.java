package com.royumana.webDashboardPlugin.events.onSignClick;

import com.royumana.webDashboardPlugin.lib.Database;
import com.royumana.webDashboardPlugin.lib.EconomyManager;
import com.royumana.webDashboardPlugin.lib.ShopRepository;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class shopClick implements Listener {

    private final ShopRepository shopRepository;
    private final EconomyManager economyManager;

    public shopClick() {
        Database db = new Database();
        this.shopRepository = new ShopRepository(db);
        this.economyManager = new EconomyManager(db);
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || !(clickedBlock.getState() instanceof Sign)) return;

        Player buyer = event.getPlayer();
        Location loc = clickedBlock.getLocation();

        int x = loc.getBlockX();
        int y = loc.getBlockY() - 1;
        int z = loc.getBlockZ();
        String world = loc.getWorld().getName();

        Map<String, Object> shop = shopRepository.getShopByLocation(world, x, y, z);
        if (shop == null) return;

        double price = (double) ((Integer) shop.get("price"));
        Material itemType = Material.matchMaterial((String) shop.get("itemName"));
        String ownerName = (String) shop.get("ownerName");

        double buyerBalance = economyManager.getBalance(buyer.getUniqueId());
        if (buyerBalance < price) {
            buyer.sendMessage("§c残高が足りません！ (所持金: " + buyerBalance + " / 必要: " + price + ")");
            return;
        }

        Block chestBlock = loc.getWorld().getBlockAt(x, y, z);
        if (!(chestBlock.getState() instanceof Chest chest)) return;

        if (!chest.getInventory().contains(itemType)) {
            buyer.sendMessage("§c在庫切れです！");
            return;
        }

        chest.getInventory().removeItem(new ItemStack(itemType, 1));
        buyer.getInventory().addItem(new ItemStack(itemType, 1));

        economyManager.addMoney(buyer.getUniqueId(), -price);

        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerName);
        if (owner != null) {
            economyManager.addMoney(owner.getUniqueId(), price);
        }

        buyer.sendMessage("§a購入完了！ §f" + itemType.name() + " を §e" + price + "コイン §fで購入しました。");
    }
}