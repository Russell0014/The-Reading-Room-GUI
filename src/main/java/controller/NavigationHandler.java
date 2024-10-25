package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;

public class NavigationHandler {

    public static void handleHomeAction(Stage stage, Model model) {
        FXMLLoader loader = new FXMLLoader(NavigationHandler.class.getResource("/view/HomeView.fxml"));
        HomeController homeController = new HomeController(stage, model);
        loader.setController(homeController);

        try {
            VBox root = loader.load();
            homeController.showStage(root);
            stage.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void handleShopAction(Stage stage, Model model) {
        FXMLLoader loader = new FXMLLoader(NavigationHandler.class.getResource("/view/ShopView.fxml"));
        ShopController shopController = new ShopController(stage, model);
        loader.setController(shopController);

        try {
            VBox root = loader.load();
            shopController.showStage(root);
            stage.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
