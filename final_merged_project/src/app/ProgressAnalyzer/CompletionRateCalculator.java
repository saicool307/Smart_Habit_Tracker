package app.ProgressAnalyzer;

import app.user.Habit;

public class CompletionRateCalculator {
    public double calculate(Habit habit) {
        return habit.getCompletionRate();
    }
}
