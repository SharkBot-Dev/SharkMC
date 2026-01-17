package com.royumana.webDashboardPlugin.commands.moderation;

import com.royumana.webDashboardPlugin.lib.register_command;
import com.royumana.webDashboardPlugin.lib.register_permission;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class echo {
    public echo() {
        new register_permission().registerPermission("webDashboardPlugin.moderation.echo_use", "発言コマンドを使用できます。", PermissionDefault.OP);

        new register_command().registerCommand("echo", new Command("echo") {
            {
                setDescription("サーバーに発言させます。");
                setUsage("/echo [発言]");
                setPermission("webDashboardPlugin.moderation.echo_use");
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("プレイヤーのみ実行可能です。");
                    return true;
                }

                if (!player.hasPermission("webDashboardPlugin.moderation.echo_use")) {
                    player.sendMessage("§c権限がありません。");
                    return true;
                }

                String text = (args.length > 0) ? String.join(" ", args) : "なし";

                sender.getServer().sendMessage(Component.text(text));
                return true;
            }
        });
    }
}
