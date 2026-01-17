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

public class setnick {
    public setnick() {
        new register_permission().registerPermission("webDashboardPlugin.moderation.setnick", "ニックネームを設定できます。", PermissionDefault.OP);

        new register_command().registerCommand("setnick", new Command("setnick") {
            {
                setDescription("プレイヤーのニックネームを変更します。");
                setUsage("/setnick <プレイヤー名> <ニックネーム>");
                setPermission("webDashboardPlugin.moderation.setnick");
                setPermissionMessage("§c権限がありません。");
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (!sender.hasPermission("webDashboardPlugin.moderation.setnick")) {
                    sender.sendMessage("§c権限がありません。");
                    return true;
                }

                if (args.length != 2) {
                    sender.sendMessage("§c使用法: /setnick [プレイヤー名] [ニックネーム]");
                    return true;
                }

                String targetName = args[0];
                String newNickName = args[1]; // 修正: args[0]からargs[1]へ

                Player target = Bukkit.getPlayer(targetName);

                if (target == null) {
                    sender.sendMessage("§cプレイヤー " + targetName + " はオンラインではありません。");
                    return true;
                }

                Component nameComponent = Component.text(newNickName);

                target.displayName(nameComponent);

                target.playerListName(nameComponent);

                target.customName(nameComponent);
                target.setCustomNameVisible(true);

                sender.sendMessage("§a" + target.getName() + " のニックネームを " + newNickName + " に設定しました。");

                return true;
            }
        });
    }
}