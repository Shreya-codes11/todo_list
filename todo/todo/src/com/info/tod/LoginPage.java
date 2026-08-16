package com.info.tod;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage extends Application {

    public static String loggedUserEmail = "";

    @Override
    public void start(Stage stage) {

        // ===== TITLE =====
        Label title = new Label("Welcome Back");
        title.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 30px; -fx-font-weight: bold;");

        // ===== USERNAME =====
        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-text-fill: #94a3b8;");

        TextField username = new TextField();
        username.setPromptText("Enter username");
        username.setStyle(inputStyle());

        // ===== EMAIL =====
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: #94a3b8;");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");
        emailField.setStyle(inputStyle());

        // ===== PASSWORD =====
        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-text-fill: #94a3b8;");

        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");
        password.setStyle(inputStyle());

        // ===== BUTTON =====
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(buttonStyle());

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: #f87171;");

        // ===== LOGIN ACTION =====
        loginBtn.setOnAction(e -> {

            String user = username.getText();
            String email = emailField.getText();
            String pass = password.getText();

            if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                msg.setText("⚠ Please fill all fields");
                return;
            }

            loggedUserEmail = email;

            msg.setStyle("-fx-text-fill: #4ade80;");
            msg.setText("Login successful!");

            // close login page
            stage.close();

            // open todo app
            try {
                new TodoApp().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ===== CARD UI =====
        VBox card = new VBox(12,
                title,
                userLabel, username,
                emailLabel, emailField,
                passLabel, password,
                loginBtn,
                msg
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setStyle(cardStyle());

        // ===== ROOT =====
        StackPane root = new StackPane(card);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #0f172a, #020617);"
        );

        Scene scene = new Scene(root, 450, 520);

        stage.setScene(scene);
        stage.setTitle("Login Page");
        stage.show();
    }

    // ===== STYLES =====
    private String inputStyle() {
        return "-fx-background-color:#1e293b;" +
                "-fx-text-fill:#e2e8f0;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#334155;" +
                "-fx-border-radius:10;" +
                "-fx-padding:10;";
    }

    private String buttonStyle() {
        return "-fx-background-color: linear-gradient(to right, #6366f1, #22c55e);" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-padding:10 25;" +
                "-fx-background-radius:12;";
    }

    private String cardStyle() {
        return "-fx-background-color: rgba(30,41,59,0.95);" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: #334155;" +
                "-fx-border-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 20, 0.3, 0, 0);";
    }

    public static void main(String[] args) {
        launch();
    }
}