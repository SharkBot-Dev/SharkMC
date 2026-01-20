package com.royumana.webDashboardPlugin.commands.economy;

import com.royumana.webDashboardPlugin.WebDashboardPlugin;
import com.royumana.webDashboardPlugin.lib.ModuleManager;
import com.royumana.webDashboardPlugin.lib.register_command;
import com.royumana.webDashboardPlugin.lib.register_permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class balance {
    public balance() {
        new register_permission().registerPermission("webDashboardPlugin.economy.balance", "現在残高を取得できます。", PermissionDefault.OP);

        new register_command().registerCommand("balance", new Command("balance") {
            {
                setDescription("現在残高を確認します。");
                setUsage("/balance [プレイヤー]");
                setPermission("webDashboardPlugin.economy.balance");
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                WebDashboardPlugin plugin = (WebDashboardPlugin) Bukkit.getPluginManager().getPlugin("WebDashboardPlugin");

                ModuleManager moduleManager = plugin.getModuleManager();
                boolean isEnabled = moduleManager.isEnabled("economy");
                if (!isEnabled) {
                    sender.sendMessage(ChatColor.RED + "経済モジュールが無効化されています。");
                    return true;
                }

                if (args.length == 0) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行可能です。");
                        return true;
                    }
                    Player player = (Player) sender;
                    double balance = plugin.getEconomyManager().getBalance(player.getUniqueId());
                    player.sendMessage(ChatColor.GOLD + "現在の所持金: " + ChatColor.WHITE + balance + "コイン");
                    return true;
                }

                if (args.length == 1) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                    if (!target.hasPlayedBefore() && !target.isOnline()) {
                        sender.sendMessage(ChatColor.RED + "そのプレイヤーは見つかりませんでした。");
                        return true;
                    }

                    double balance = plugin.getEconomyManager().getBalance(target.getUniqueId());
                    sender.sendMessage(ChatColor.YELLOW + target.getName() + " の所持金: " + ChatColor.WHITE + balance + "円");
                    return true;
                }

                return false;
            }
        });
    }
}
