package app.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HabitCollection implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Habit> habits = new ArrayList<>();

    public void add(Habit h) { habits.add(h); }

    public void remove(Habit h) { habits.remove(h); }

    public List<Habit> getAll() { return habits; }

    public Optional<Habit> findByName(String name) {
        return habits.stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
