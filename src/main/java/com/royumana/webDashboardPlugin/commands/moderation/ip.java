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

public class ip {
    public ip() {
        String command_name = "ip";
        String perm_name = "webDashboardPlugin.moderation.ip";

        new register_permission().registerPermission(perm_name, "IPを開示できます。", PermissionDefault.OP);

        new register_command().registerCommand(command_name, new Command(command_name) {
            {
                setDescription("IPを開示します。");
                setUsage("/ip [プレイヤー]");
                setPermission(perm_name);
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (args.length == 0) {
                    sender.sendMessage("プレイヤー名を指定してください。");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    try {
                        String ip = target.getAddress().getHostString();
                        sender.sendMessage(target.getName() + " のIPアドレスは " + ip + " です。");
                    } catch (NullPointerException np){
                        sender.sendMessage(target.getName() + " IPアドレスを開示できませんでした。");
                        return true;
                    }
                } else {
                    sender.sendMessage("プレイヤーが見つかりません。");
                }
                return true;
            }
        });
    }
}
