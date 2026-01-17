package com.royumana.webDashboardPlugin.lib;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class register_permission {
    public void registerPermission(String permName, String descripsion, PermissionDefault def) {
        if (Bukkit.getPluginManager().getPermission(permName) != null) {
            // すでに存在する場合は何もしない
            return;
        }

        Permission permission = new Permission(
                permName,
                descripsion,
                def
        );

        Bukkit.getPluginManager().addPermission(permission);
    }
}
