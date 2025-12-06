package app.ReminderEngine;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Simple scheduler that triggers a ReminderNotifier at the same time every day.
 * Uses a single-thread ScheduledExecutorService.
 */
public class ReminderScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, ScheduledFuture<?>> scheduled = new HashMap<>();
    private final ReminderNotifier notifier = new ReminderNotifier();

    /**
     * Schedule a daily reminder for the given habit name at the provided LocalTime.
     * If a reminder for the given habit already exists it will be canceled and rescheduled.
     */
    public void scheduleDaily(String habitName, LocalTime time) {
        cancel(habitName);

        long initialDelay = computeInitialDelay(time);
        long period = TimeUnit.DAYS.toSeconds(1);

        ScheduledFuture<?> f = scheduler.scheduleAtFixedRate(
                () -> notifier.notifyReminder(habitName),
                initialDelay, period, TimeUnit.SECONDS);

        scheduled.put(habitName, f);
    }

    private long computeInitialDelay(LocalTime target) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = LocalDateTime.of(LocalDate.now(), target);
        if (next.isBefore(now) || next.isEqual(now)) {
            next = next.plusDays(1);
        }
        Duration d = Duration.between(now, next);
        return d.getSeconds();
    }

    public void cancel(String habitName) {
        ScheduledFuture<?> f = scheduled.remove(habitName);
        if (f != null) f.cancel(false);
    }

    /** Cancel all scheduled reminders and shutdown the executor. */
    public void shutdown() {
        for (ScheduledFuture<?> f : scheduled.values()) {
            if (f != null) f.cancel(false);
        }
        scheduled.clear();
        scheduler.shutdownNow();
    }
}
