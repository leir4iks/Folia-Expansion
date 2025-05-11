package org.dreeam.expansion.folia;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class FoliaExpansion extends PlaceholderExpansion {

    private FoliaUtils foliaUtils = null;

    private final Cache<String, Integer> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Override
    public boolean canRegister() {
        return true;
    }

    public void clear() {
        foliaUtils = null;
        cache.invalidateAll();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "folia";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Dreeam__";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    public Map<String, Object> getDefaults() {
        final Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.putIfAbsent("tps_color.high", "&a");
        defaults.putIfAbsent("tps_color.medium", "&e");
        defaults.putIfAbsent("tps_color.low", "&c");
        return defaults;
    }

    private @Nullable String getCached(String key, Callable<Integer> callable) {
        try {
            return String.valueOf(cache.get(key, callable));
        } catch (ExecutionException e) {
            return "";
        }
    }

    @Override
    public String onRequest(OfflinePlayer p, @NotNull String identifier) {
        if (foliaUtils == null) {
            foliaUtils = new FoliaUtils();
            foliaUtils.checkFolia();
        }

        if (!foliaUtils.isFolia) return null;

        if (p == null || !p.isOnline()) return "";
        Player player = p.getPlayer();
        if (player == null) return "";

        // --- REGION PLACEHOLDERS ---
        if (identifier.equalsIgnoreCase("region_tps")) {
            List<Double> tps = foliaUtils.getTPS(player.getLocation());
            return fixTPS(tps.get(0)); // 5s TPS региона
        }
        if (identifier.equalsIgnoreCase("region_mspt")) {
            List<Double> mspt = foliaUtils.getMSPT(player.getLocation());
            return fixMSPT(mspt.get(0)); // 5s MSPT региона
        }
        if (identifier.equalsIgnoreCase("region_tps_colored")) {
            List<Double> tps = foliaUtils.getTPS(player.getLocation());
            return getColoredTPS(tps.get(0));
        }
        if (identifier.equalsIgnoreCase("region_mspt_colored")) {
            List<Double> mspt = foliaUtils.getMSPT(player.getLocation());
            return getColoredMSPT(mspt.get(0));
        }

        // --- Глобальные плейсхолдеры и остальные ---
        switch (identifier) {
            case "global_tps":
                return getFoliaGlobalTPS(null);
            case "global_mspt":
                return getFoliaGlobalMSPT(null);
            case "global_util":
                return getFoliaGlobalUtil(null);
            case "tps":
                return getFoliaTPS(null, player.getLocation());
            case "mspt":
                return getFoliaMSPT(null, player.getLocation());
            case "util":
                return getFoliaUtil(null, player.getLocation());
        }

        if (identifier.startsWith("global_tps_")) {
            identifier = identifier.replace("global_tps_", "");
            return getFoliaGlobalTPS(identifier);
        }

        if (identifier.startsWith("global_mspt_")) {
            identifier = identifier.replace("global_mspt_", "");
            return getFoliaGlobalMSPT(identifier);
        }

        if (identifier.startsWith("global_util_")) {
            identifier = identifier.replace("global_util_", "");
            return getFoliaGlobalUtil(identifier);
        }

        if (identifier.startsWith("tps_")) {
            identifier = identifier.replace("tps_", "");
            return getFoliaTPS(identifier, player.getLocation());
        }

        if (identifier.startsWith("mspt_")) {
            identifier = identifier.replace("mspt_", "");
            return getFoliaMSPT(identifier, player.getLocation());
        }

        if (identifier.startsWith("util_")) {
            identifier = identifier.replace("util_", "");
            return getFoliaUtil(identifier, player.getLocation());
        }

        return null;
    }

    // --- Остальной код без изменений (getFoliaGlobalTPS, getFoliaGlobalMSPT, getFoliaGlobalUtil, getFoliaTPS, getFoliaMSPT, getFoliaUtil, toLegacy, fixTPS, getColoredTPS, fixMSPT, getColoredMSPT, и т.д.) ---

    // ... (оставь остальной код как у тебя)
    // Ниже примеры для fixTPS, fixMSPT и getColoredTPS, getColoredMSPT

    private String fixTPS(double tps) {
        String finalTPS = String.format("%.2f", tps);
        return (tps > 20.00 ? "*" : "") + finalTPS;
    }

    private String getColoredTPS(double tps) {
        return toLegacy(Component.text(fixTPS(tps), getColourForTPS(tps)));
    }

    private String fixMSPT(double mspt) {
        return String.format("%.2f", mspt);
    }

    private String getColoredMSPT(double mspt) {
        return toLegacy(Component.text(fixMSPT(mspt), getColourForMSPT(mspt)));
    }

    private String toLegacy(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component).replaceAll("&", "§");
    }

    private NamedTextColor getColourForTPS(double tps) {
        if (tps >= 18.0) return NamedTextColor.GREEN;
        if (tps >= 15.0) return NamedTextColor.YELLOW;
        return NamedTextColor.RED;
    }

    private NamedTextColor getColourForMSPT(double mspt) {
        if (mspt <= 50.0) return NamedTextColor.GREEN;
        if (mspt <= 100.0) return NamedTextColor.YELLOW;
        return NamedTextColor.RED;
    }
}
