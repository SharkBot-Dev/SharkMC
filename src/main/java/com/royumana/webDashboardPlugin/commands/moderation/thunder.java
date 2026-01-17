package com.royumana.webDashboardPlugin.commands.moderation;

import com.royumana.webDashboardPlugin.lib.register_command;
import com.royumana.webDashboardPlugin.lib.register_permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class thunder {
    public thunder() {
        new register_permission().registerPermission("webDashboardPlugin.moderation.thunder", "発言コマンドを使用できます。", PermissionDefault.OP);

        new register_command().registerCommand("thunder", new Command("thunder") {
            {
                setDescription("プレイヤーの位置に雷を出します。");
                setUsage("/thunder [プレイヤー]");
                setPermission("webDashboardPlugin.moderation.thunder");
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("プレイヤーのみ実行可能です。");
                    return true;
                }

                String text = (args.length > 0) ? String.join(" ", args) : "なし";

                try {
                    Player arg_player = ((Player) sender).getServer().getPlayer(text);
                    arg_player.getWorld().strikeLightning(arg_player.getLocation());
                } catch (NullPointerException np) {
                    sender.sendMessage("そのプレイヤーが見つかりません。");
                    return true;
                }

                return true;
            }
        });
    }
}
