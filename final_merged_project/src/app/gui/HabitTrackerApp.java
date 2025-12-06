package app.gui;

import app.HabitTrackerManager.*;
import app.ProgressAnalyzer.*;
import app.ReminderEngine.*;
import app.user.*;
import app.util.*;
import app.exception.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

public class HabitTrackerApp {
    private final AccountManager accountManager = new AccountManager();
    private final LeaderboardManager leaderboardManager = new LeaderboardManager(accountManager);
    private ReminderScheduler reminderScheduler = new ReminderScheduler();

    private UserProfile currentUser;
    private HabitManager habitManager;
    private StatisticsManager statsManager;

    private JFrame frame;
    private DefaultListModel<String> habitListModel;
    private JList<String> habitList;
    private JTextArea detailsArea;
    private JTextField inputHabitName;
    private JTextField targetCompletionsField;
    private JTextField targetStreakField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HabitTrackerApp().showLogin());
    }

    private void showLogin() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JTextField usernameField = new JTextField();
        p.add(new JLabel("Enter username:"));
        p.add(usernameField);
        int option = JOptionPane.showConfirmDialog(null, p, "Login / Sign Up", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;
        String username = usernameField.getText().trim();
        if (username.isEmpty()) { JOptionPane.showMessageDialog(null, "Username required"); showLogin(); return; }
        try {
            try {
                currentUser = accountManager.login(username);
            } catch (AccountException ae) {
                currentUser = accountManager.signUp(username);
                JOptionPane.showMessageDialog(null, "Account created for " + username);
            }
            habitManager = new HabitManager(currentUser);
            statsManager = new StatisticsManager(currentUser);
            createAndShowGUI();
        } catch (AccountException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            showLogin();
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Smart Habit Tracker - " + currentUser.getUsername());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,700);
        frame.setLayout(new BorderLayout());

        // Left - habits
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(new EmptyBorder(8,8,8,8));
        habitListModel = new DefaultListModel<>();
        habitList = new JList<>(habitListModel);
        habitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        habitList.setFixedCellWidth(240);
        JScrollPane listScroll = new JScrollPane(habitList);
        left.add(listScroll, BorderLayout.CENTER);

        JPanel addPanel = new JPanel(new GridLayout(0,1,4,4));
        inputHabitName = new JTextField();
        addPanel.add(new JLabel("New habit name:"));
        addPanel.add(inputHabitName);

        JButton addBtn = new JButton("Add Habit");
        addBtn.addActionListener(e -> onAddHabit());
        addPanel.add(addBtn);

        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.addActionListener(e -> onRemoveHabit());
        addPanel.add(removeBtn);

        JButton markBtn = new JButton("Mark Completed Today");
        markBtn.addActionListener(e -> onMarkComplete());
        addPanel.add(markBtn);

        left.add(addPanel, BorderLayout.SOUTH);

        // Right - details and dashboard
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(new EmptyBorder(8,8,8,8));
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        right.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JPanel controls = new JPanel(new GridLayout(0,1,4,4));
        JPanel targetPanel = new JPanel(new GridLayout(0,1));
        targetCompletionsField = new JTextField();
        targetStreakField = new JTextField();
        targetPanel.add(new JLabel("Set target completions (optional):"));
        targetPanel.add(targetCompletionsField);
        targetPanel.add(new JLabel("Set target streak (days, optional):"));
        targetPanel.add(targetStreakField);
        controls.add(targetPanel);

        JButton setTargetsBtn = new JButton("Set Targets for Selected");
        setTargetsBtn.addActionListener(e -> onSetTargets());
        controls.add(setTargetsBtn);

        JButton statsBtn = new JButton("Show Selected Stats / Graph");
        statsBtn.addActionListener(e -> showSelectedStats());
        controls.add(statsBtn);

        JButton leaderboardBtn = new JButton("Show Leaderboard");
        leaderboardBtn.addActionListener(e -> showLeaderboard());
        controls.add(leaderboardBtn);

        JButton exportBtn = new JButton("Export CSV");
        exportBtn.addActionListener(e -> exportCSV());
        controls.add(exportBtn);

        JButton saveBtn = new JButton("Save Data");
        saveBtn.addActionListener(e -> onSave());
        controls.add(saveBtn);

        JButton reminderBtn = new JButton("Reminders & Scheduler");
        reminderBtn.addActionListener(e -> openRemindersDialog());
        controls.add(reminderBtn);

        right.add(controls, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(320);
        frame.add(split, BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Welcome, " + currentUser.getUsername()));
        top.add(new JLabel(" | Avg completion rate: " + String.format("%.2f%%", statsManager.averageCompletionRate())));
        frame.add(top, BorderLayout.NORTH);

        habitList.addListSelectionListener(e -> updateDetails());
        refreshHabitList();
        frame.setVisible(true);
    }

    private void refreshHabitList() {
        habitListModel.clear();
        List<Habit> habits = habitManager.getHabits();
        for (Habit h : habits) habitListModel.addElement(h.getName());
    }

    private void updateDetails() {
        String sel = habitList.getSelectedValue();
        if (sel == null) {
            detailsArea.setText("Select a habit to view details.");
            return;
        }
        try {
            Habit h = habitManager.getHabitByName(sel);
            StringBuilder sb = new StringBuilder();
            // StreakCalculator sb = new StreakCalculator();
            sb.append("Habit: ").append(h.getName()).append("\n");
            sb.append("Streak: ").append(h.getStreak()).append("\n");
            sb.append("Total completions: ").append(h.getTotalCompletions()).append("\n");
            sb.append(String.format("Completion rate: %.2f%%\n", h.getCompletionRate()));
            sb.append("Last completed: ").append(h.getLastCompletedDate()).append("\n");
            sb.append("Targets: completions=").append(h.getTargetCompletions())
              .append(", streak=").append(h.getTargetStreak()).append("\n");
            detailsArea.setText(sb.toString());
        } catch (HabitException ex) {
            detailsArea.setText("Error: " + ex.getMessage());
        }
    }

    private void onAddHabit() {
        String name = inputHabitName.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(frame, "Enter a habit name."); return; }
        try {
            habitManager.addHabit(name);
            refreshHabitList();
            inputHabitName.setText("");
        } catch (HabitException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }

    private void onRemoveHabit() {
        String sel = habitList.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(frame, "Select a habit to remove."); return; }
        try {
            habitManager.removeHabit(sel);
            refreshHabitList();
            detailsArea.setText("");
        } catch (HabitException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }

    private void onMarkComplete() {
        String sel = habitList.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(frame, "Select a habit."); return; }
        try {
            Habit h = habitManager.getHabitByName(sel);
            String result = h.markComplete();
            JOptionPane.showMessageDialog(frame, result);
            updateDetails();
        } catch (HabitException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }

    private void onSetTargets() {
        String sel = habitList.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(frame, "Select a habit."); return; }
        try {
            Habit h = habitManager.getHabitByName(sel);
            String completionsText = targetCompletionsField.getText().trim();
            String streakText = targetStreakField.getText().trim();
            if (!completionsText.isEmpty()) {
                try { int t = Integer.parseInt(completionsText); h.setTargetCompletions(t); }
                catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid completions number."); return; }
            }
            if (!streakText.isEmpty()) {
                try { int s = Integer.parseInt(streakText); h.setTargetStreak(s); }
                catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid streak number."); return; }
            }
            JOptionPane.showMessageDialog(frame, "Targets set.");
            updateDetails();
        } catch (HabitException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }

    private void showSelectedStats() {
    String sel = habitList.getSelectedValue();
    if (sel == null) {
        JOptionPane.showMessageDialog(frame, "Select a habit.");
        return;
    }
    try {
        Habit h = habitManager.getHabitByName(sel);

        CompletionRateCalculator crc = new CompletionRateCalculator();
        HabitPatternDetector hpd = new HabitPatternDetector();

        StringBuilder sb = new StringBuilder();
        sb.append("Habit: ").append(h.getName()).append("\n");
        sb.append("Completion rate: ").append(String.format("%.2f%%", crc.calculate(h))).append("\n");
        sb.append("Streak: ").append(h.getStreak()).append("\n");
        sb.append("Longest detected run: ").append(hpd.longestRun(h)).append(" days\n");

        // simple trend: last 7 days
        List<Integer> points = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            final int dayOffset = i;
            boolean done = h.getHistory().getCompletedDates().stream()
                    .anyMatch(d -> d.equals(java.time.LocalDate.now().minusDays(dayOffset)));
            points.add(done ? 1 : 0);
        }

        SimpleLineChart chart = new SimpleLineChart(points);
        chart.setTitle("Last 7 days (1 = completed)");

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);

        panel.add(new JScrollPane(ta), BorderLayout.NORTH);
        panel.add(chart, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(450, 350));

        JOptionPane.showMessageDialog(frame, panel, "Stats & Graph", JOptionPane.PLAIN_MESSAGE);

    } catch (HabitException ex) {
        JOptionPane.showMessageDialog(frame, ex.getMessage());
    }
}


    private void showLeaderboard() {
        List<LeaderboardManager.UserScore> top = leaderboardManager.getTopUsers(10);
        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (LeaderboardManager.UserScore us : top) {
            sb.append(String.format("%d. %s - %d\n", rank++, us.getUsername(), us.getScore()));
        }
        JOptionPane.showMessageDialog(frame, sb.length() == 0 ? "No users yet." : sb.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(currentUser.getUsername() + "_habits.csv"));
        int res = fc.showSaveDialog(frame);
        if (res != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();
        try {
            CSVExporter.exportUser(currentUser, file);
            JOptionPane.showMessageDialog(frame, "Exported to " + file.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Export failed: " + e.getMessage());
        }
    }

    private void onSave() {
        try {
            accountManager.save();
            JOptionPane.showMessageDialog(frame, "Saved successfully.");
        } catch (DataException e) {
            JOptionPane.showMessageDialog(frame, "Failed to save: " + e.getMessage());
        }
    }

    private void openRemindersDialog() {
        String sel = habitList.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(frame, "Select a habit to set a reminder."); return; }
        String timeStr = JOptionPane.showInputDialog(frame, "Enter reminder time (HH:mm) e.g., 18:30");
        if (timeStr == null) return;
        try {
            LocalTime t = LocalTime.parse(timeStr);
            reminderScheduler.scheduleDaily(sel, t);
            JOptionPane.showMessageDialog(frame, "Reminder scheduled daily at " + t);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid time format.");
        }
    }
}
