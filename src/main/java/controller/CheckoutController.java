package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Model;

import java.util.Calendar;

public class CheckoutController {
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
    private Text total;

    @FXML
    private TextField nameOnCard;
    @FXML
    private TextField cardNumber;
    @FXML
    private TextField expDate;
    @FXML
    private TextField cvv;
    @FXML
    private Button pay;
    @FXML
    private Label error;


    public CheckoutController(Stage parentStage, Model model) {
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

        total.setText("Total: $ " + model.getCartTotal());

        // Set error label style to red
        error.setStyle("-fx-text-fill: red;");

        // Add listeners for input validation
        cardNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cardNumber.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 16) {
                cardNumber.setText(oldValue);
            }
        });

        cvv.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cvv.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 3) {
                cvv.setText(oldValue);
            }
        });

        // Add forward slash automatically after MM in expDate
        expDate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*/?\\d*")) {
                expDate.setText(newValue.replaceAll("[^\\d/]", ""));
            }
            if (newValue.length() == 2 && oldValue.length() == 1) {
                expDate.setText(newValue + "/");
            }
            if (newValue.length() > 5) {
                expDate.setText(oldValue);
            }
        });

        pay.setOnAction(e -> {
            error.setText("");  // Clear previous error messages

            // Check for empty fields
            if (nameOnCard.getText().isEmpty() || cardNumber.getText().isEmpty() ||
                    expDate.getText().isEmpty() || cvv.getText().isEmpty()) {
                error.setText("Please fill out all fields");
                return;
            }

            // Validate card number
            if (cardNumber.getText().length() != 16) {
                error.setText("Card number must be 16 digits");
                return;
            }

            // Validate CVV
            if (cvv.getText().length() != 3) {
                error.setText("CVV must be 3 digits");
                return;
            }

            // Validate expiration date format and value
            String expDateText = expDate.getText();
            if (!expDateText.matches("\\d{2}/\\d{2}")) {
                error.setText("Expiration date must be in the format MM/YY");
                return;
            }

            // Simple date validation
            String[] dateParts = expDateText.split("/");
            int month = Integer.parseInt(dateParts[0]);
            int year = Integer.parseInt(dateParts[1]);

            // Get current year and month
            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR) % 100;
            int currentMonth = now.get(Calendar.MONTH) + 1;

            // Validate month
            if (month < 1 || month > 12) {
                error.setText("Invalid month (must be 01-12)");
                return;
            }

            // Check if date is in the past
            if (year < currentYear || (year == currentYear && month <= currentMonth)) {
                error.setText("Card has expired");
                return;
            }

            // Loop through cart items and add them to orders
            model.viewCart().forEach(cartItem -> {
                model.addOrder(model.getCurrentUser().getUsername(), cartItem.getBookName(), cartItem.getQuantity(), cartItem.getPrice());
            });

            // If all validations pass
            model.clearCart();

            error.setStyle("-fx-text-fill: green;"); // Success message in green
            error.setText("Payment successful!");

            NavigationHandler.handleThankyouAction(stage, model);

        });

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Home");
        stage.show();
    }
}
