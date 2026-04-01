package io.julia0x.atlas.integration.logging;

import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Monitors performance metrics for various components.
 * Tracks execution times and resource usage.
 */
public class PerformanceMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceMonitor.class);

    private static final class Metric {
        long totalTime = 0;
        long count = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = 0;

        synchronized void record(long time) {
            totalTime += time;
            count++;
            minTime = Math.min(minTime, time);
            maxTime = Math.max(maxTime, time);
        }

        synchronized double getAverageTime() {
            return count == 0 ? 0 : (double) totalTime / count;
        }
    }

    private final Map<String, Metric> metrics = new ConcurrentHashMap<>();
    private final Map<String, Long> startTimes = new ConcurrentHashMap<>();
    private boolean enabled = false;

    /**
     * Enables or disables performance monitoring.
     *
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            LOGGER.info("Performance monitoring enabled");
        } else {
            LOGGER.info("Performance monitoring disabled");
        }
    }

    /**
     * Starts timing a named operation.
     *
     * @param name The operation name
     */
    public void startTimer(String name) {
        if (enabled) {
            startTimes.put(name, System.nanoTime());
        }
    }

    /**
     * Stops timing an operation and records the time.
     *
     * @param name The operation name
     */
    public void stopTimer(String name) {
        if (!enabled) {
            return;
        }

        Long startTime = startTimes.remove(name);
        if (startTime != null) {
            long duration = System.nanoTime() - startTime;
            metrics.computeIfAbsent(name, k -> new Metric()).record(duration);
        }
    }

    /**
     * Records a time measurement.
     *
     * @param name The metric name
     * @param nanoSeconds The time in nanoseconds
     */
    public void record(String name, long nanoSeconds) {
        if (enabled) {
            metrics.computeIfAbsent(name, k -> new Metric()).record(nanoSeconds);
        }
    }

    /**
     * Gets the average execution time for a metric.
     *
     * @param name The metric name
     * @return Average time in milliseconds
     */
    public double getAverageTimeMs(String name) {
        Metric metric = metrics.get(name);
        return metric != null ? metric.getAverageTime() / 1_000_000.0 : 0;
    }

    /**
     * Gets the total execution count for a metric.
     *
     * @param name The metric name
     * @return The count
     */
    public long getCount(String name) {
        Metric metric = metrics.get(name);
        return metric != null ? metric.count : 0;
    }

    /**
     * Clears all recorded metrics.
     */
    public void clear() {
        metrics.clear();
        startTimes.clear();
    }

    /**
     * Logs all collected metrics.
     */
    public void logStatistics() {
        if (metrics.isEmpty()) {
            LOGGER.info("No performance metrics recorded");
            return;
        }

        LOGGER.info("=== Performance Metrics ===");
        for (Map.Entry<String, Metric> entry : metrics.entrySet()) {
            Metric metric = entry.getValue();
            double avgMs = metric.getAverageTime() / 1_000_000.0;
            double minMs = metric.minTime / 1_000_000.0;
            double maxMs = metric.maxTime / 1_000_000.0;
            LOGGER.info(String.format("  %s: avg=%.3fms, min=%.3fms, max=%.3fms, count=%d",
                entry.getKey(), avgMs, minMs, maxMs, metric.count));
        }
    }

    /**
     * Gets a copy of all metrics for analysis.
     *
     * @return Map of metric names to their statistics
     */
    public Map<String, String> getMetricsReport() {
        Map<String, String> report = new LinkedHashMap<>();
        for (Map.Entry<String, Metric> entry : metrics.entrySet()) {
            Metric metric = entry.getValue();
            double avgMs = metric.getAverageTime() / 1_000_000.0;
            report.put(entry.getKey(), String.format("%.3fms (x%d)", avgMs, metric.count));
        }
        return report;
    }
}
