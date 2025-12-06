package app.ReminderEngine;

import java.time.LocalTime;

public class ReminderValidator {
    public boolean isValidTime(LocalTime t) {
        return t != null;
    }

    public boolean isValidHabitName(String name) {
        return name != null && !name.trim().isEmpty();
    }
}
