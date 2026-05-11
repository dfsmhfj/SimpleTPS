package com.dfsmhfj.simpletps;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.LinkedList;

public class TPSCalculator {

    private static double serverTPS = 20.0;
    private static double serverMSPT = 50.0;

    // 用于计算瞬时值
    private final LinkedList<Long> tickDurations = new LinkedList<>();
    private static final int WINDOW_SIZE = 20;

    // 指数平滑
    private double smoothedTPS = 20.0;
    private double smoothedMSPT = 50.0;
    private static final double SMOOTHING = 0.05;

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        long averageTickNanos = server.getAverageTickTimeNanos();

        tickDurations.add(averageTickNanos);
        if (tickDurations.size() > WINDOW_SIZE) {
            tickDurations.removeFirst();
        }

        long totalNanos = 0;
        for (long d : tickDurations) {
            totalNanos += d;
        }
        double avgMS = (totalNanos / (double) tickDurations.size()) / 1_000_000.0;

        double instantaneousTPS = Math.min(20.0, 1000.0 / avgMS);

        // 平滑处理
        smoothedTPS = smoothedTPS * (1 - SMOOTHING) + instantaneousTPS * SMOOTHING;
        smoothedMSPT = avgMS; // MSPT 直接使用平均值已足够平滑

        serverTPS = smoothedTPS;
        serverMSPT = smoothedMSPT;
    }

    public static double getServerTPS() {
        return serverTPS;
    }

    public static double getServerMSPT() {
        return serverMSPT;
    }
}