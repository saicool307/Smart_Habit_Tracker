package app.ReminderEngine;

import java.io.Serializable;
import java.time.LocalTime;

public class ReminderRuleSet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String habitName;
    private LocalTime reminderTime;
    private boolean enabled;

    public ReminderRuleSet(String habitName, LocalTime reminderTime, boolean enabled) {
        this.habitName = habitName;
        this.reminderTime = reminderTime;
        this.enabled = enabled;
    }

    public String getHabitName() { return habitName; }
    public LocalTime getReminderTime() { return reminderTime; }
    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setReminderTime(LocalTime reminderTime) { this.reminderTime = reminderTime; }
}
