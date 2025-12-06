package app.ProgressAnalyzer;

import app.user.Habit;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

/**
 * Simple detector that identifies the longest consecutive run
 * (best streak) present in the habit history.
 */
public class HabitPatternDetector {

    public int longestRun(Habit habit) {
        Set<LocalDate> dates = new TreeSet<>(habit.getHistory().getCompletedDates());
        int best = 0;
        int current = 0;
        LocalDate prev = null;
        for (LocalDate d : dates) {
            if (prev == null || d.equals(prev.plusDays(1))) {
                current++;
            } else {
                current = 1;
            }
            best = Math.max(best, current);
            prev = d;
        }
        return best;
    }
}
