package app.HabitTrackerManager;

import app.user.Habit;
import app.user.UserProfile;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsManager {
    private final UserProfile user;
    public StatisticsManager(UserProfile user) { this.user = user; }

    public double averageCompletionRate() {
        List<Habit> habits = user.getHabitCollection().getAll();
        if (habits.isEmpty()) return 0.0;
        double sum = 0;
        for (Habit h : habits) sum += h.getCompletionRate();
        return sum / habits.size();
    }

    public List<Habit> topStreaks(int limit) {
        return user.getHabitCollection().getAll().stream()
                .sorted(Comparator.comparingInt(Habit::getStreak).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
