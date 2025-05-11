package org.dreeam.expansion.folia;

import org.bukkit.plugin.java.JavaPlugin;

public class FoliaExpansionPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Регистрируем PlaceholderExpansion
        new FoliaExpansion().register();
        getLogger().info("FoliaExpansion enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("FoliaExpansion disabled!");
    }
}
