package app.HabitTrackerManager;

import app.user.UserProfile;

import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardManager {
    private final AccountManager accountManager;
    public LeaderboardManager(AccountManager accountManager) { this.accountManager = accountManager; }

    public List<UserScore> getTopUsers(int limit) {
        Map<String, UserProfile> users = accountManager.getAllUsers();
        List<UserScore> scores = new ArrayList<>();
        for (UserProfile up : users.values()) {
            int total = up.getHabitCollection().getAll().stream().mapToInt(h -> h.getTotalCompletions()).sum();
            scores.add(new UserScore(up.getUsername(), total));
        }
        return scores.stream().sorted(Comparator.comparingInt(UserScore::getScore).reversed())
                .limit(limit).collect(Collectors.toList());
    }

    public static class UserScore {
        private final String username;
        private final int score;
        public UserScore(String username, int score) { this.username = username; this.score = score; }
        public String getUsername(){return username;} public int getScore(){return score;}
    }
}
