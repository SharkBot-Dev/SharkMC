package com.royumana.webDashboardPlugin.events;

import com.royumana.webDashboardPlugin.events.onBlockBreak.protectShop;
import com.royumana.webDashboardPlugin.events.onPlayerChat.ChatLogManager;
import com.royumana.webDashboardPlugin.events.onSignClick.shopClick;
import org.bukkit.plugin.java.JavaPlugin;

public class events_registers {
    public events_registers(JavaPlugin plugin) {
        // onPlayerChat
        plugin.getServer().getPluginManager().registerEvents(new ChatLogManager(), plugin);

        // onBlockBreak
        plugin.getServer().getPluginManager().registerEvents(new protectShop(), plugin);

        // onSignClick
        plugin.getServer().getPluginManager().registerEvents(new shopClick(), plugin);
    }
}
