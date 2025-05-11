package org.dreeam.expansion.folia;

import io.papermc.paper.threadedregions.TickRegions;
import io.papermc.paper.threadedregions.ThreadedRegionizer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_4_R1.CraftWorld;
import net.minecraft.world.level.Level;

import java.util.List;

public class FoliaUtils {

    public boolean isFolia = false;

    /**
     * Проверяет, запущен ли сервер на Folia (наличие класса RegionizedServer).
     */
    public void checkFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
    }

    /**
     * Получить глобальный TPS сервера (1, 5, 15 минут).
     * Возвращает 5 значений, дублируя последние.
     */
    public List<Double> getGlobalTPS() {
        double[] tps = Bukkit.getServer().getTPS();
        return List.of(tps[0], tps[1], tps[2], tps[2], tps[2]);
    }

    /**
     * Получить глобальный MSPT сервера (среднее время тика).
     * Возвращает 5 значений, дублируя текущее среднее.
     */
    public List<Double> getGlobalMSPT() {
        double mspt = Bukkit.getServer().getAverageTickTime();
        return List.of(mspt, mspt, mspt, mspt, mspt);
    }

    /**
     * Заглушка - глобальная загрузка сервера.
     */
    public double getGlobalUtil() {
        return 1.0;
    }

    /**
     * Получить TPS региона по локации игрока.
     * Если Folia не используется или данные недоступны - возвращает глобальный TPS.
     */
    public List<Double> getTPS(Location location) {
        if (!isFolia || location == null) {
            return getGlobalTPS();
        }
        var region = getRegion(location);
        if (region == null || region.getData() == null) {
            return getGlobalTPS();
        }
        var handle = region.getData().getRegionSchedulingHandle();
        long now = System.nanoTime();
        return List.of(
                handle.getTickReport5s(now).tpsData().segmentAll().average(),
                handle.getTickReport15s(now).tpsData().segmentAll().average(),
                handle.getTickReport1m(now).tpsData().segmentAll().average(),
                handle.getTickReport5m(now).tpsData().segmentAll().average(),
                handle.getTickReport15m(now).tpsData().segmentAll().average()
        );
    }

    /**
     * Получить MSPT региона по локации игрока.
     * Если Folia не используется или данные недоступны - возвращает глобальный MSPT.
     */
    public List<Double> getMSPT(Location location) {
        if (!isFolia || location == null) {
            return getGlobalMSPT();
        }
        var region = getRegion(location);
        if (region == null || region.getData() == null) {
            return getGlobalMSPT();
        }
        var handle = region.getData().getRegionSchedulingHandle();
        long now = System.nanoTime();
        return List.of(
                handle.getTickReport5s(now).timePerTickData().segmentAll().average() / 1_000_000.0,
                handle.getTickReport15s(now).timePerTickData().segmentAll().average() / 1_000_000.0,
                handle.getTickReport1m(now).timePerTickData().segmentAll().average() / 1_000_000.0,
                handle.getTickReport5m(now).timePerTickData().segmentAll().average() / 1_000_000.0,
                handle.getTickReport15m(now).timePerTickData().segmentAll().average() / 1_000_000.0
        );
    }

    /**
     * Получить регион Folia по локации игрока.
     */
    private ThreadedRegionizer.ThreadedRegion<TickRegions.TickRegionData, TickRegions.TickRegionSectionData> getRegion(Location location) {
        if (location == null) return null;
        World bukkitWorld = location.getWorld();
        if (bukkitWorld == null) return null;

        Level nmsLevel = ((CraftWorld) bukkitWorld).getHandle();
        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;

        var regionizer = nmsLevel.regioniser;
        return regionizer.getRegion(chunkX, chunkZ);
    }
}
