package com.info.tod;

import java.sql.Date;

public class Task {

    private int id;
    private String task;
    private boolean completed;
    private String priority;
    private Date dueDate;

    // NEW: points for each task
    private int points;

    // Constructor
    public Task(int id, String task, boolean completed, String priority, Date dueDate) {
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.priority = priority;
        this.dueDate = dueDate;
        this.points = 10; // default points per task
    }

    // Getters
    public int getId() { return id; }
    public String getTask() { return task; }
    public boolean isCompleted() { return completed; }
    public String getPriority() { return priority; }
    public Date getDueDate() { return dueDate; }
    public int getPoints() { return points; }

    // Setter for completion
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Setter for points (optional)
    public void setPoints(int points) {
        this.points = points;
    }

    // 🔥 Important: for ListView display
    @Override
    public String toString() {
        return task + " | Priority: " + priority +
               " | Due: " + dueDate +
               (completed ? " ✔ Completed" : " ❌ Pending");
    }
}