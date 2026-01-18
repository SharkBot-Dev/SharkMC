package com.royumana.webDashboardPlugin.events;

import com.royumana.webDashboardPlugin.events.onPlayerChat.ChatLogManager;
import org.bukkit.plugin.java.JavaPlugin;

public class events_registers {
    public events_registers(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ChatLogManager(), plugin);
    }
}
