/*
 * FramePerSecondLimiter.java
 * Author: Seokjin Yoon
 * Created Date: 2020-02-23
 */

package com.thunder_cut.netio;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FramePerSecondLimiter {
    private ScheduledExecutorService scheduledExecutorService;
    private boolean enabled;

    public FramePerSecondLimiter() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        enabled = true;
    }

    /**
     * Start a FramePerSecondLimiter
     *
     * @param fps frame per second
     */
    public void start(long fps) {
        long period = 1000 / fps;
        scheduledExecutorService.scheduleAtFixedRate(this::run, 0, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop a FramePerSecondLimiter
     */
    public void stop() {
        scheduledExecutorService.shutdown();
    }

    private void run() {
        enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
