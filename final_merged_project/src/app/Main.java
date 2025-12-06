package app;

import java.util.Scanner;
import app.gui.HabitTrackerApp;
import app.console.ConsoleApp;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println(" SMART HABIT TRACKER ");
        System.out.println("====================================");
        System.out.println("Select mode:");
        System.out.println("1. GUI Mode");
        System.out.println("2. Console Mode");
        System.out.print("Enter choice: ");

        String choice = sc.nextLine().trim();

        if (choice.equals("1")) {
            System.out.println("Launching GUI...");

            // Run GUI in a SEPARATE thread – prevents Scanner from blocking it
            new Thread(() -> {
                try {
                    HabitTrackerApp.main(new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        else if (choice.equals("2")) {
            System.out.println("Launching Console Mode...\n");
            ConsoleApp.start();
        }
        else {
            System.out.println("Invalid option. Exiting.");
        }
    }
}
