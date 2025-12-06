package app.ProgressAnalyzer;

import app.user.Habit;

import java.time.LocalDate;
import java.util.Set;

public class HistoryAnalyzer {

    /**
     * Counts the number of completions in the inclusive date range.
     */
    public int countCompletionsInRange(Habit habit, LocalDate fromInclusive, LocalDate toInclusive) {
        Set<LocalDate> dates = habit.getHistory().getCompletedDates();
        int count = 0;
        for (LocalDate d : dates) {
            if ((d.isEqual(fromInclusive) || d.isAfter(fromInclusive)) &&
                (d.isEqual(toInclusive)   || d.isBefore(toInclusive))) {
                count++;
            }
        }
        return count;
    }
}
