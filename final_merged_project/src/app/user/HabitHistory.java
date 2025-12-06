package app.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HabitHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Set<LocalDate> completedDates = new HashSet<>();

    public void recordCompletion(LocalDate date) {
        completedDates.add(date);
    }

    public boolean isCompletedOn(LocalDate date) {
        return completedDates.contains(date);
    }

    public Set<LocalDate> getCompletedDates() {
        return completedDates;
    }
}
