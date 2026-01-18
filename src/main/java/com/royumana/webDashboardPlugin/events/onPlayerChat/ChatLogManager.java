package com.royumana.webDashboardPlugin.events.onPlayerChat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.LinkedList;
import java.util.List;

public class ChatLogManager implements Listener {
    // 最新100件のログを保持
    private static final LinkedList<String> chatHistory = new LinkedList<>();
    private final int MAX_LOGS = 100;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String entry = String.format("[%s] %s: %s",
                new java.util.Date().toString(),
                event.getPlayer().getName(),
                event.getMessage());

        synchronized (chatHistory) {
            if (chatHistory.size() >= MAX_LOGS) {
                chatHistory.removeFirst();
            }
            chatHistory.addLast(entry);
        }
    }

    public static List<String> getLogs() {
        synchronized (chatHistory) {
            return new LinkedList<>(chatHistory);
        }
    }

    public static void addLogs(String text) {
        chatHistory.add(text);
    }
}