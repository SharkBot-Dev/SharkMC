package com.royumana.webDashboardPlugin.lib;

import java.util.ArrayList;
import java.util.List;

public class SidebarManager {
    private static final List<MenuEntry> externalMenus = new ArrayList<>();

    public static class MenuEntry {
        public final String name;
        public final String url;
        public MenuEntry(String name, String url) { this.name = name; this.url = url; }
    }

    public static void addMenu(String name, String url) {
        externalMenus.add(new MenuEntry(name, url));
    }

    public static List<MenuEntry> getMenus() {
        return externalMenus;
    }
}