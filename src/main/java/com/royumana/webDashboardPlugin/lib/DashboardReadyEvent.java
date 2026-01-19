package com.royumana.webDashboardPlugin.lib;

import com.royumana.webDashboardPlugin.WebDashboardPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DashboardReadyEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final WebDashboardPlugin api;

    public DashboardReadyEvent(WebDashboardPlugin api) {
        this.api = api;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}