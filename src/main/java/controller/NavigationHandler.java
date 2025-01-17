package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
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

    public static void handleLoginAction(Stage stage, Model model) {
        model.setCurrentUser(null);
        try {
            stage.close();
            FXMLLoader loader = new FXMLLoader(NavigationHandler.class.getResource("/view/LoginView.fxml"));
            LoginController loginController = new LoginController(stage, model);
            loader.setController(loginController);
            GridPane root = loader.load();
            loginController.showStage(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void handleCartAction(Stage stage, Model model) {
        FXMLLoader loader = new FXMLLoader(NavigationHandler.class.getResource("/view/CartView.fxml"));
        CartController cartController = new CartController(stage, model);
        loader.setController(cartController);

        try {
            VBox root = loader.load();
            cartController.showStage(root);
            stage.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void handleCheckoutAction(Stage stage, Model model) {
        FXMLLoader loader = new FXMLLoader(NavigationHandler.class.getResource("/view/CheckoutView.fxml"));
        CheckoutController checkoutController = new CheckoutController(stage, model);
        loader.setController(checkoutController);

        try {
            VBox root = loader.load();
            checkoutController.showStage(root);
            stage.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void handleThankyouAction(Stage stage, Model model) {
        FXMLLoader loader = new FXMLLoader(NavigationHandler.class.getResource("/view/ThankyouView.fxml"));
        ThankyouController thankyouController = new ThankyouController(stage, model);
        loader.setController(thankyouController);

        try {
            VBox root = loader.load();
            thankyouController.showStage(root);
            stage.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void handleOrdersAction(Stage stage, Model model) {
        FXMLLoader loader = new FXMLLoader(NavigationHandler.class.getResource("/view/OrdersView.fxml"));
        OrdersController ordersController = new OrdersController(stage, model);
        loader.setController(ordersController);

        try {
            VBox root = loader.load();
            ordersController.showStage(root);
            stage.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void handleUpdateProfileAction(Stage stage, Model model) {
        FXMLLoader loader = new FXMLLoader(NavigationHandler.class.getResource("/view/UpdateProfileView.fxml"));
        UpdateProfileController updateProfileController = new UpdateProfileController(stage, model);
        loader.setController(updateProfileController);

        try {
            VBox root = loader.load();
            updateProfileController.showStage(root);
            stage.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
