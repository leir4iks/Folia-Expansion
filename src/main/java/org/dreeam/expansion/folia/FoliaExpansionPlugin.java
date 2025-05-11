package org.dreeam.expansion.folia;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FoliaExpansionPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Проверяем, что PlaceholderAPI установлен и активен
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            // Регистрируем расширение PlaceholderAPI
            new FoliaExpansion().register();
            getLogger().info("FoliaExpansion enabled!");
        } else {
            getLogger().severe("PlaceholderAPI not found! Disabling FoliaExpansion.");
            // Отключаем плагин, если PlaceholderAPI отсутствует
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("FoliaExpansion disabled!");
    }
}
