package app.ReminderEngine;

import javax.swing.*;

public class ReminderNotifier {
    /**
     * Shows a Swing dialog reminding the user to perform the habit.
     * This uses SwingUtilities.invokeLater to ensure it's shown on the EDT.
     */
    public void notifyReminder(String habitName) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                    "Reminder: time to do your habit → " + habitName,
                    "Habit Reminder",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
