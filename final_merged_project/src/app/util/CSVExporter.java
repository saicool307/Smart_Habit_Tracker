package app.util;

import app.user.*;
import java.io.*;
import java.util.List;

public class CSVExporter {

    /**
     * Exports a simple CSV summary of the user's habits.
     * Columns: habit,streak,totalCompletions,totalDays,completionRate
     */
    public static void exportUser(UserProfile user, File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("habit,streak,totalCompletions,totalDays,completionRate");

            List<Habit> habits = user.getHabitCollection().getAll();
            for (Habit h : habits) {
                // Replace commas in name to avoid CSV column shifts.
                String safeName = h.getName().replace(',', ' ');
                pw.printf("%s,%d,%d,%d,%.2f%n",
                        safeName,
                        h.getStreak(),
                        h.getTotalCompletions(),
                        h.getTotalDaysTracked(),
                        h.getCompletionRate());
            }
        }
    }
}
