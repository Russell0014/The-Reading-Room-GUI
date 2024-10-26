package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Model;
import model.Orders;
import utils.WrappingTableCell;

import java.util.List;

public class OrdersController {
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
    private TableView<Orders> tableView;
    @FXML
    private TableColumn<Orders, String> bookName;
    @FXML
    private TableColumn<Orders, String> bookPrice;
    @FXML
    private TableColumn<Orders, String> orderQuantity;
    @FXML
    private TableColumn<Orders, String> orderDate;
    @FXML
    private Text total;

    public OrdersController(Stage parentStage, Model model) {
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

        loadOrders();

        total.setText(String.format("Total: $ %.2f", model.getOrderTotal()));

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Shop");
        stage.show();
    }

    public void loadOrders() {
        List<Orders> orders = model.viewOrders();
        ObservableList<Orders> observableList = FXCollections.observableArrayList(orders);

        bookName.setCellFactory(WrappingTableCell::new);

        bookName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookName()));
        bookPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f",
                        cellData.getValue().getPrice())));
        orderQuantity.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
        orderDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderDate()));

        tableView.setItems(observableList);
    }


}
