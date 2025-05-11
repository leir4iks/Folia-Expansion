package org.dreeam.expansion.folia;

import io.papermc.paper.threadedregions.TickRegionScheduler;
import io.papermc.paper.threadedregions.TickRegions;
import io.papermc.paper.threadedregions.ThreadedRegionizer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21.4_R1.CraftWorld;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

public class FoliaUtils {

    public boolean isFolia = false;

    public void checkFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
    }

    public List<Double> getGlobalTPS() {
        double[] tps = Bukkit.getServer().getTPS();
        return List.of(tps[0], tps[1], tps[2], tps[2], tps[2]);
    }

    public List<Double> getGlobalMSPT() {
        double mspt = Bukkit.getServer().getAverageTickTime();
        return List.of(mspt, mspt, mspt, mspt, mspt);
    }

    public double getGlobalUtil() {
        return 1.0;
    }

    public List<Double> getTPS(Location location) {
        if (!isFolia || location == null) {
            return getGlobalTPS();
        }
        ThreadedRegionizer.ThreadedRegion<TickRegions.TickRegionData, TickRegions.TickRegionSectionData> region = getRegion(location);
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

    public List<Double> getMSPT(Location location) {
        if (!isFolia || location == null) {
            return getGlobalMSPT();
        }
        ThreadedRegionizer.ThreadedRegion<TickRegions.TickRegionData, TickRegions.TickRegionSectionData> region = getRegion(location);
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
     * Получить регион по локации игрока.
     */
    private ThreadedRegionizer.ThreadedRegion<TickRegions.TickRegionData, TickRegions.TickRegionSectionData> getRegion(Location location) {
        if (location == null) return null;
        World bukkitWorld = location.getWorld();
        if (bukkitWorld == null) return null;

        Level nmsLevel = ((CraftWorld) bukkitWorld).getHandle();
        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;

        // Получаем регионизатор
        var regionizer = nmsLevel.regioniser;

        // Получаем регион по координатам чанка
        return regionizer.getRegion(chunkX, chunkZ);
    }
}
