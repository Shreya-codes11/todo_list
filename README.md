To-Do List (Gamified To-Do App)

A desktop task manager built with JavaFX and MySQL that turns everyday productivity into a game — earn XP for completing tasks, level up, and get email notifications celebrating your progress.

✨ Features
Task Management — Add, edit, delete, and mark tasks as complete
Priority Levels — Tag tasks as High, Medium, or Low priority with color-coded labels
Due Dates — Assign and track deadlines with a built-in date picker
Search, Filter & Sort — Instantly search tasks, filter by completion status, and sort by priority or date
Progress Tracking — A live progress bar shows what percentage of your tasks are done
Gamification (XP & Levels) — Earn 10 XP per completed task, level up every 100 XP, and get a celebratory "Level Up" popup
Email Notifications — Automatically emails you when you complete a task, congratulating you on the points earned
Login Page — Simple username/email/password entry screen before accessing the app
Dark UI Theme — A clean, modern dark-mode interface styled with JavaFX CSS

🛠️ Tech Stack
Layer	Technology
UI	JavaFX
Backend Logic	Java
Database	MySQL (via JDBC)
Email Service	Jakarta Mail (SMTP via Gmail)

📂 Project Structure
tod/
├── TodoApp.java      # Main application window, UI, and task list logic
├── LoginPage.java     # Login screen shown before the main app
├── Task.java          # Task model (id, description, priority, due date, points)
├── User.java           # User model (username, email, points)
├── DBHelper.java      # All database operations (CRUD, points logic)
└── EmailSender.java   # Sends completion-notification emails via SMTP

⚙️ Setup & Installation
Prerequisites
Java JDK 17+
JavaFX SDK
MySQL Server running locally
A Gmail account with an App Password for email notifications

1. Clone the repository
  
bash
git clone https://github.com/<your-username>/todo_list.git
cd todo_list

2. Configure the database
Create a MySQL database named todo_app. The app will automatically create the required tasks and user_points tables on first run via DBHelper.createTable().
Update the database credentials in DBHelper.java:

private static final String URL = "jdbc:mysql://localhost:3306/todo_app...";
private static final String USER = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";

3. Configure email notifications

In EmailSender.java, set your own sender email and app password — do not hardcode real credentials in the source. Use environment variables or a separate config file instead:

final String fromEmail = System.getenv("SMTP_EMAIL");
final String password = System.getenv("SMTP_APP_PASSWORD");

4. Run the application

Compile and run LoginPage.java as the entry point (it launches TodoApp after a successful login).

🚀 Usage
Launch the app and log in with a username, email, and password
Add a task with a description, priority, and optional due date
Check off tasks as you complete them to earn XP
Watch your progress bar and level indicator update in real time
Receive an email confirmation each time you finish a task
