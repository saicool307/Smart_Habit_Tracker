package app.console;

import app.HabitTrackerManager.*;
import app.user.*;
import app.exception.*;
import app.util.*;
import app.ProgressAnalyzer.HabitPatternDetector;

import java.io.File;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    private static final AccountManager accountManager = new AccountManager();
    private static LeaderboardManager leaderboardManager;
    private static HabitManager habitManager;
    private static StatisticsManager statsManager;
    private static UserProfile currentUser;

    private static Scanner sc = new Scanner(System.in);

    public static void start() {

        leaderboardManager = new LeaderboardManager(accountManager);

        System.out.println("=== LOGIN ===");
        System.out.print("Username: ");
        String username = sc.nextLine().trim();

        try {
            try {
                currentUser = accountManager.login(username);
                System.out.println("Welcome back, " + username + "!");
            } catch (AccountException e) {
                currentUser = accountManager.signUp(username);
                System.out.println("Account created for: " + username);
            }

            habitManager = new HabitManager(currentUser);
            statsManager = new StatisticsManager(currentUser);

            runMainMenu();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void runMainMenu() {

        while (true) {
            System.out.println("\n==============================");
            System.out.println(" HABIT TRACKER - CONSOLE MODE ");
            System.out.println("==============================");
            System.out.println("1. View All Habits");
            System.out.println("2. Add Habit");
            System.out.println("3. Remove Habit");
            System.out.println("4. Mark Habit as Completed Today");
            System.out.println("5. Set Targets");
            System.out.println("6. View Habit Stats");
            System.out.println("7. View Leaderboard");
            System.out.println("8. Export CSV");
            System.out.println("9. Save & Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": viewHabits(); break;
                case "2": addHabit(); break;
                case "3": removeHabit(); break;
                case "4": markCompleted(); break;
                case "5": setTargets(); break;
                case "6": viewStats(); break;
                case "7": viewLeaderboard(); break;
                case "8": exportCSV(); break;
                case "9": saveAndExit(); return;
                default: System.out.println("Invalid choice."); 
            }
        }
    }

    private static void viewHabits() {
        List<Habit> habits = habitManager.getHabits();
        System.out.println("\n=== YOUR HABITS ===");

        if (habits.isEmpty()) {
            System.out.println("No habits yet.");
            return;
        }

        for (Habit h : habits) {
            System.out.println("- " + h.getName() + " (streak " + h.getStreak() + ")");
        }
    }

    private static void addHabit() {
        System.out.print("Enter habit name: ");
        String name = sc.nextLine().trim();

        try {
            habitManager.addHabit(name);
            System.out.println("Habit added!");
        } catch (HabitException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void removeHabit() {
        System.out.print("Enter habit name to remove: ");
        String name = sc.nextLine().trim();
        try {
            habitManager.removeHabit(name);
            System.out.println("Habit removed.");
        } catch (HabitException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void markCompleted() {
        System.out.print("Enter habit name: ");
        String name = sc.nextLine().trim();
        try {
            Habit h = habitManager.getHabitByName(name);
            String msg = h.markComplete();
            System.out.println(msg);
        } catch (HabitException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void setTargets() {
        System.out.print("Enter habit name: ");
        String name = sc.nextLine().trim();
        try {
            Habit h = habitManager.getHabitByName(name);

            System.out.print("Target completions (or blank): ");
            String tc = sc.nextLine();
            if (!tc.isBlank()) h.setTargetCompletions(Integer.parseInt(tc));

            System.out.print("Target streak (or blank): ");
            String ts = sc.nextLine();
            if (!ts.isBlank()) h.setTargetStreak(Integer.parseInt(ts));

            System.out.println("Targets updated!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewStats() {
        System.out.print("Enter habit name: ");
        String name = sc.nextLine().trim();

        try {
            Habit h = habitManager.getHabitByName(name);

            System.out.println("\n=== Habit Stats ===");
            System.out.println("Name: " + h.getName());
            System.out.println("Streak: " + h.getStreak());
            System.out.println("Total Completions: " + h.getTotalCompletions());
            System.out.println("Completion Rate: " + String.format("%.2f%%", h.getCompletionRate()));
            System.out.println("Longest Run: " + new HabitPatternDetector().longestRun(h));

        } catch (HabitException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewLeaderboard() {
        System.out.println("\n=== Leaderboard ===");
        List<LeaderboardManager.UserScore> scores = leaderboardManager.getTopUsers(10);

        if (scores.isEmpty()) {
            System.out.println("No users yet.");
            return;
        }

        int rank = 1;
        for (LeaderboardManager.UserScore us : scores) {
            System.out.println(rank++ + ". " + us.getUsername() + " - " + us.getScore());
        }
    }

    private static void exportCSV() {
        System.out.print("Enter file name (example: report.csv): ");
        String fileName = sc.nextLine();
        try {
            CSVExporter.exportUser(currentUser, new File(fileName));
            System.out.println("Exported.");
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    private static void saveAndExit() {
        try {
            accountManager.save();
            System.out.println("Saved. Goodbye!");
        } catch (DataException e) {
            System.out.println("Failed to save: " + e.getMessage());
        }
    }
}
