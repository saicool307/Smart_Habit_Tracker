package app.user;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private HabitCollection habitCollection = new HabitCollection();
    private UserStatistics statistics = new UserStatistics();

    public UserProfile(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }

    public HabitCollection getHabitCollection() { return habitCollection; }

    public UserStatistics getStatistics() { return statistics; }
}
