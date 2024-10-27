package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Model;

import java.sql.SQLException;

public class UpdateProfileController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML
    private MenuItem updateProfile;
    @FXML
    private MenuItem home;
    @FXML
    private MenuItem shop;
    @FXML
    private MenuItem cart;
    @FXML
    private MenuItem orders;
    @FXML
    private MenuItem logout;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Button save;


    public UpdateProfileController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        home.setOnAction(e -> NavigationHandler.handleHomeAction(stage, model));
        shop.setOnAction(e -> NavigationHandler.handleShopAction(stage, model));
        cart.setOnAction(e -> NavigationHandler.handleCartAction(stage, model));
        orders.setOnAction(e -> NavigationHandler.handleOrdersAction(stage, model));
        logout.setOnAction(e -> NavigationHandler.handleLoginAction(stage, model));

        // Prefill the form with current user data
        if (model.getCurrentUser() != null) {
            firstName.setText(model.getCurrentUser().getFirstName());
            lastName.setText(model.getCurrentUser().getLastName());
        }

        // Add save button handler
        save.setOnAction(e -> handleSave());

    }

    private void handleSave() {
        String newFirstName = firstName.getText().trim();
        String newLastName = lastName.getText().trim();
        String newPassword = password.getText();
        String confirmPass = confirmPassword.getText();

        // If password fields are not empty, validate password match
        if (!newPassword.isEmpty() || !confirmPass.isEmpty()) {
            if (!newPassword.equals(confirmPass)) {
                showAlert("Error", "Passwords do not match.");
                return;
            }
            if (newPassword.length() > 8) {
                showAlert("Error", "Password must not exceed 8 characters.");
                return;
            }
        }

        try {
            model.updateUserProfile(newFirstName, newLastName, newPassword);
            showAlert("Success", "Profile updated successfully!");

            // Clear password fields after successful update
            password.clear();
            confirmPassword.clear();
        } catch (SQLException ex) {
            showAlert("Error", "Failed to update profile: " + ex.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Home");
        stage.show();
    }
}
