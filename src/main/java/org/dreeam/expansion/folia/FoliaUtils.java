package org.dreeam.expansion.folia;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Collections;
import java.util.List;

public class FoliaUtils {

    public boolean isFolia = false;

    /**
     * Проверяет, запущен ли сервер на Folia, по наличию класса internal API.
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
     * Получить глобальную загрузку (Util) - заглушка, всегда 1.0 (100%).
     */
    public double getGlobalUtil() {
        return 1.0;
    }

    /**
     * Получить TPS для региона по локации - отсутствует в публичном API, возвращаем глобальный TPS.
     */
    public List<Double> getTPS(Location location) {
        return getGlobalTPS();
    }

    /**
     * Получить MSPT для региона по локации - отсутствует в публичном API, возвращаем глобальный MSPT.
     */
    public List<Double> getMSPT(Location location) {
        return getGlobalMSPT();
    }

    /**
     * Получить Util для региона по локации - заглушка, возвращаем глобальный Util.
     */
    public List<Double> getUtil(Location location) {
        return Collections.singletonList(getGlobalUtil());
    }

    /**
     * Возвращает максимальное количество потоков - заглушка, возвращает 1.
     */
    public double maxThreadsCount() {
        return 1.0;
    }
}
