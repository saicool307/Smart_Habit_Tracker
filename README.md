****Smart Habit Tracker****


A complete habit tracking system built in Java that supports both GUI (Swing) and Console (CLI) modes.
This project demonstrates strong Object-Oriented Programming, modular design, file persistence, analytics, and user interface development.

🚀 **Features**

✔ **Dual Mode Application**

   At startup, the user chooses:
   
   GUI Mode (interactive Swing interface)
   
   Console Mode (terminal-based habit tracker)

✔ **User Accounts**

 Sign up / Login
 
 Persistent user data (.ser file)
 
 Multiple users supported

✔ **Habit Management**

 Add habits
 
 Remove habits
 
 Mark habits as completed
 
 Track daily activity
 
 Display streaks and total completions

✔ **Smart Statistics & Analytics**

 Completion rate (%)
 
 Current streak
 
 Longest streak (pattern detection)
 
 Weekly progress trend graph (custom Swing chart)
 
 Historical analysis

✔ **Goal Tracking**

 Set:
 
 Target streak
 
 Target completion count
 Automatic congratulations when goals are achieved 🎉

✔ **Leaderboard System**

 Ranks all users based on total completions.

✔ **Reminder Engine**
 
 Daily reminders
 
 Custom time input (HH:mm)
 
 Notification popup (Swing)

 ✔ **CSV Export**

Exports habit statistics in a readable format:

habit, streak, totalCompletions, totalDays, completionRate

✔ **Clean & Modular Architecture**

app

 ├── gui               # Swing-based UI
 
 ├── console           # Console (CLI) mode
 
 ├── user              # Data models (Habit, UserProfile, History)
 
 ├── HabitTrackerManager
 
 ├── ProgressAnalyzer
 
 ├── ReminderEngine
 
 ├── util
 
 └── exception         # Custom exceptions


 
🖥️ **Technology Stack**

| Component | Technology             |
| --------- | ---------------------- |
| GUI       | Java Swing + AWT       |
| Console   | Java Standard I/O      |
| Language  | Java 8+                |
| Storage   | Java Serialization     |
| Graphics  | Custom Swing rendering |
| Build     | javac / manual build   |

📦 **Installation & Running**

1️⃣ **Compile the project**

Windows PowerShell:

javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object {$_.FullName})

macOS / Linux:

javac -d out $(find src -name "*.java")

2️⃣ **Run the main launcher**

1 → GUI Mode

2 → Console Mode

📄 **Future Enhancements**

Dark/light mode support

JavaFX UI rewrite

Mobile version (Android)

SQLite database storage

Cloud backup
