package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Model;

public class CheckoutController {
    private Model model;
    private Stage stage;
    private Stage parentStage;
    @FXML
    private MenuItem viewProfile;
    @FXML
    private MenuItem updateProfile;
    @FXML
    private MenuItem home;
    @FXML
    private MenuItem shop;
    @FXML
    private MenuItem cart;
    @FXML
    private MenuItem logout;
    @FXML
    private Text total;


    public CheckoutController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    // Add your code to complete the functionality of the program
    @FXML
    public void initialize() {

        home.setOnAction(e -> NavigationHandler.handleHomeAction(stage, model));
        shop.setOnAction(e -> NavigationHandler.handleShopAction(stage, model));
        cart.setOnAction(e -> NavigationHandler.handleCartAction(stage, model));
        logout.setOnAction(e -> NavigationHandler.handleLoginAction(stage, model));

        total.setText("Total: $ " + model.getCartTotal());

    }


    public void showStage(Pane root) {
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Home");
        stage.show();
    }
}
