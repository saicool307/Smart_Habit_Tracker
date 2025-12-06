package app.HabitTrackerManager;

import app.user.Habit;
import app.user.UserProfile;
import app.exception.HabitException;

import java.util.List;
import java.util.Optional;

public class HabitManager {
    private final UserProfile user;

    public HabitManager(UserProfile user) { this.user = user; }

    public void addHabit(String name) throws HabitException {
        Optional<Habit> existing = user.getHabitCollection().findByName(name);
        if (existing.isPresent()) throw new HabitException("Habit already exists.");
        user.getHabitCollection().add(new Habit(name));
    }

    public void removeHabit(String name) throws HabitException {
        Optional<Habit> existing = user.getHabitCollection().findByName(name);
        if (!existing.isPresent()) throw new HabitException("Habit not found.");
        user.getHabitCollection().remove(existing.get());
    }

    public List<Habit> getHabits() { return user.getHabitCollection().getAll(); }

    public Habit getHabitByName(String name) throws HabitException {
        return user.getHabitCollection().findByName(name)
                .orElseThrow(() -> new HabitException("Habit not found."));
    }
}
