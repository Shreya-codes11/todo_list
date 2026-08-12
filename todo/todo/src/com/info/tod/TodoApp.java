package com.info.tod;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class TodoApp extends Application {
	int lastLevel=1;

    private ListView<HBox> listView = new ListView<>();
    private ProgressBar progressBar = new ProgressBar(0);
    private Label percentLabel = new Label();

    private TextField searchField = new TextField();
    private ComboBox<String> filterBox = new ComboBox<>();
    private ComboBox<String> sortBox = new ComboBox<>();

    private VBox pointsCard; // 🔥 NEW

    private String cardStyle() {
        return "-fx-background-color: #1e293b;" +
               "-fx-background-radius: 12;" +
               "-fx-border-color: #334155;" +
               "-fx-border-radius: 12;";
    }

    // 🎮 POINTS UI CARD
    private VBox createPointsCard() {

        Label title = new Label("Your Progress");
        title.setStyle("-fx-text-fill:#94a3b8; -fx-font-size:14px;");

        Label pointsBig = new Label("0 XP");
        pointsBig.setStyle("-fx-text-fill:white; -fx-font-size:32px; -fx-font-weight:bold;");

        Label levelLabel = new Label("Level 1");
        levelLabel.setStyle("-fx-text-fill:#22c55e; -fx-font-size:14px;");

        ProgressBar levelBar = new ProgressBar(0);
        levelBar.setPrefHeight(8);
        levelBar.setStyle("-fx-accent:#22c55e;");

        VBox card = new VBox(8, title, pointsBig, levelLabel, levelBar);
        card.setPadding(new Insets(15));

        card.setStyle(
                "-fx-background-color: linear-gradient(to right, #1e293b, #0f172a);" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: #334155;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(34,197,94,0.3), 20, 0.5, 0, 0);"
        );

        card.setUserData(new Object[]{pointsBig, levelLabel, levelBar});

        return card;
    }
    private void showLevelUp(int level, String taskName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Level Up!");
        alert.setHeaderText("🎉 Congratulations!");
        alert.setContentText("You completed: " + taskName + "\nYou reached Level " + level + " 🚀");
        alert.show();
    }
    private void updatePointsUI() {
        try {
            int points = DBHelper.getPoints();

            Object[] refs = (Object[]) pointsCard.getUserData();

            Label pointsBig = (Label) refs[0];
            Label levelLabel = (Label) refs[1];
            ProgressBar levelBar = (ProgressBar) refs[2];

            int level = (points / 100) + 1;
            double progress = (points % 100) / 100.0;

            pointsBig.setText(points + " XP");
            levelLabel.setText("Level " + level);
            levelBar.setProgress(progress);

            // 🔥 LEVEL UP DETECTION (FIXED)
            if (level > lastLevel) {

                // get latest completed task name safely
                String taskName = DBHelper.getLastCompletedTask(); 
                if (taskName == null) {
                    taskName = "Task";
                }

                showLevelUp(level, taskName);
                lastLevel = level;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void load() {
        try {
            listView.getItems().clear();

            var tasks = DBHelper.getTasks();
            sortTasks(tasks);

            int completed = 0;

            for (Task t : tasks) {

                if (!t.getTask().toLowerCase().contains(searchField.getText().toLowerCase()))
                    continue;

                if ("Completed".equals(filterBox.getValue()) && !t.isCompleted()) continue;
                if ("Pending".equals(filterBox.getValue()) && t.isCompleted()) continue;

                HBox row = new HBox(15);
                row.setPadding(new Insets(12));
                row.setStyle(cardStyle());

                CheckBox check = new CheckBox();
                check.setSelected(t.isCompleted());

                Label label = new Label(t.getTask());
                label.setStyle("-fx-font-size:15px; -fx-text-fill:#e2e8f0;");

                if (t.isCompleted()) {
                    label.setStyle("-fx-font-size:15px; -fx-text-fill:#64748b; -fx-strikethrough:true;");
                    completed++;
                }

                Label dateLabel = new Label(
                        t.getDueDate() == null ? "No Date" : t.getDueDate().toString()
                );
                dateLabel.setStyle("-fx-text-fill:#94a3b8; -fx-font-size:12px;");

                VBox taskInfo = new VBox(4, label, dateLabel);
                HBox.setHgrow(taskInfo, Priority.ALWAYS);

                Label priorityLabel = new Label(t.getPriority());

                String color = switch (t.getPriority()) {
                    case "High" -> "#ef4444";
                    case "Medium" -> "#f59e0b";
                    default -> "#22c55e";
                };

                priorityLabel.setStyle(
                        "-fx-background-color:" + color + ";" +
                        "-fx-text-fill:white;" +
                        "-fx-padding:4 10;" +
                        "-fx-background-radius:10;"
                );

                Button editBtn = new Button("✏");
                Button delBtn = new Button("🗑");

                editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill:#6366f1;-fx-font-size:20px;");
                delBtn.setStyle("-fx-background-color: transparent; -fx-text-fill:#ef4444;-fx-font-size:20px;");

                check.setOnAction(e -> {
                    try {
                        boolean isNowCompleted = check.isSelected();

                        DBHelper.toggleTask(t.getId(), isNowCompleted);

                        if (isNowCompleted) {
                            EmailSender.sendMail(LoginPage.loggedUserEmail, 10);
                        }

                        load();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                delBtn.setOnAction(e -> {
                    try {
                        DBHelper.deleteTask(t.getId());
                        load();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                editBtn.setOnAction(e -> {
                    TextInputDialog dialog = new TextInputDialog(t.getTask());
                    dialog.setHeaderText("Edit Task");
                    dialog.showAndWait().ifPresent(newText -> {
                        try {
                            DBHelper.updateTask(t.getId(), newText);
                            load();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                });

                HBox buttons = new HBox(10, editBtn, delBtn);
                row.getChildren().addAll(check, taskInfo, priorityLabel, buttons);
                listView.getItems().add(row);
            }

            double progress = tasks.size() == 0 ? 0 : (double) completed / tasks.size();
            progressBar.setProgress(progress);
            percentLabel.setText((int)(progress * 100) + "% Completed");

            updatePointsUI(); // 🔥 UPDATE UI

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortTasks(List<Task> tasks) {

        String selected = sortBox.getValue();

        if ("Sort by Priority".equals(selected)) {
            tasks.sort((t1, t2) -> {
                Map<String, Integer> map = Map.of(
                        "High", 3,
                        "Medium", 2,
                        "Low", 1
                );
                return map.get(t2.getPriority()) - map.get(t1.getPriority());
            });
        }

        else if ("Sort by Date".equals(selected)) {
            tasks.sort((t1, t2) -> {
                if (t1.getDueDate() == null) return 1;
                if (t2.getDueDate() == null) return -1;
                return t1.getDueDate().compareTo(t2.getDueDate());
            });
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        DBHelper.createTable();

        Label title = new Label("To-Do App");
        title.setStyle("-fx-font-size:30px; -fx-text-fill:#e2e8f0; -fx-font-weight:600;");

        String inputStyle =
                "-fx-background-color:#1e293b;" +
                "-fx-text-fill:#e2e8f0;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#334155;" +
                "-fx-border-radius:10;";

        searchField.setPromptText("Search...");
        searchField.setPrefHeight(40);
        searchField.setStyle(inputStyle);

        filterBox.getItems().addAll("All", "Completed", "Pending");
        filterBox.setValue("All");

        sortBox.getItems().addAll("Sort by Priority", "Sort by Date");
        sortBox.setValue("Sort by Priority");

        TextField input = new TextField();
        input.setPromptText("New task...");
        input.setPrefHeight(40);
        input.setStyle(inputStyle);

        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        priorityBox.setValue("Medium");

        DatePicker datePicker = new DatePicker();

        Button addBtn = new Button("Add Task");
        addBtn.setStyle("-fx-background-color:#6366f1; -fx-text-fill:white; -fx-background-radius:10;");

        addBtn.setOnAction(e -> {
            try {
                DBHelper.addTask(
                        input.getText(),
                        priorityBox.getValue(),
                        datePicker.getValue() == null ? null :
                                java.sql.Date.valueOf(datePicker.getValue())
                );
                input.clear();
                load();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox inputRow = new HBox(10, input, priorityBox, datePicker, addBtn);

        progressBar.setPrefHeight(20);
        progressBar.setStyle("-fx-accent:#6366f1;");
        percentLabel.setStyle("-fx-text-fill:#e2e8f0;");

        VBox progressBox = new VBox(5, percentLabel, progressBar);

        Button clearBtn = new Button("Clear Completed");
        clearBtn.setStyle("-fx-background-color:#ef4444; -fx-text-fill:white;");

        clearBtn.setOnAction(e -> {
            try {
                DBHelper.clearCompleted();
                load();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        pointsCard = createPointsCard();

        VBox layout = new VBox(15,
                title,
                pointsCard,
                searchField,
                filterBox,
                sortBox,
                inputRow,
                progressBox,
                clearBtn,
                listView
        );

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #0f172a; -fx-font-family: 'Segoe UI';");

        searchField.setOnKeyReleased(e -> load());
        filterBox.setOnAction(e -> load());
        sortBox.setOnAction(e -> load());

        stage.setScene(new Scene(layout, 750, 750));
        stage.setTitle("Gamified To-Do App");
        stage.show();

        load();
    }

    public static void main(String[] args) {
        launch();
    }
}