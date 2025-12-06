package app.user;

import java.io.Serializable;
import java.time.LocalDate;

public class Habit implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int streak;
    private int totalCompletions;
    private int totalDaysTracked;
    private LocalDate lastCompletedDate;
    private HabitHistory history;

    // Optional targets
    private Integer targetCompletions;
    private Integer targetStreak;

    public Habit(String name) {
        this.name = name;
        this.streak = 0;
        this.totalCompletions = 0;
        this.totalDaysTracked = 0;
        this.lastCompletedDate = null;
        this.history = new HabitHistory();
    }

    public String getName() { return name; }
    public int getStreak() { return streak; }
    public int getTotalCompletions() { return totalCompletions; }
    public int getTotalDaysTracked() { return totalDaysTracked; }
    public LocalDate getLastCompletedDate() { return lastCompletedDate; }
    public HabitHistory getHistory() { return history; }

    public Integer getTargetCompletions() { return targetCompletions; }
    public Integer getTargetStreak() { return targetStreak; }
    public void setTargetCompletions(Integer t) { targetCompletions = t; }
    public void setTargetStreak(Integer t) { targetStreak = t; }

    /** Mark today's completion */
    public String markComplete() {
        LocalDate today = LocalDate.now();

        // Prevent double completion for same day
        if (lastCompletedDate != null && lastCompletedDate.equals(today)) {
            return "Already marked complete today.";
        }

        // Streak continuation?
        if (lastCompletedDate != null && lastCompletedDate.plusDays(1).equals(today)) {
            streak++;
        } else {
            streak = 1; // reset to new streak
        }

        totalCompletions++;

        // Count unique tracked days
        if (lastCompletedDate == null || !lastCompletedDate.equals(today)) {
            totalDaysTracked++;
        }

        lastCompletedDate = today;
        history.recordCompletion(today);

        // Check goals
        String congratulations = checkTargets();
        return congratulations == null ? "Marked complete." : "Marked complete. " + congratulations;
    }

    private String checkTargets() {
        StringBuilder sb = new StringBuilder();

        if (targetCompletions != null && totalCompletions >= targetCompletions) {
            sb.append("🎉 Congrats! You reached ")
              .append(targetCompletions)
              .append(" completions for '")
              .append(name)
              .append("'! ");
            targetCompletions = null;
        }

        if (targetStreak != null && streak >= targetStreak) {
            sb.append("🔥 Amazing! You hit a streak of ")
              .append(targetStreak)
              .append(" days for '")
              .append(name)
              .append("'! ");
            targetStreak = null;
        }

        return sb.length() == 0 ? null : sb.toString();
    }

    public double getCompletionRate() {
        if (totalDaysTracked == 0) return 0;
        return (totalCompletions * 100.0) / totalDaysTracked;
    }

    @Override
    public String toString() {
        return name + " (streak: " + streak + ", completions: " + totalCompletions + ")";
    }
}
