package app.user;

import java.io.Serializable;

public class UserStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalCompletionsAllHabits;

    public int getTotalCompletionsAllHabits() {
        return totalCompletionsAllHabits;
    }

    public void addCompletions(int n) {
        totalCompletionsAllHabits += n;
    }
}
