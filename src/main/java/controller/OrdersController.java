package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import model.Orders;
import utils.WrappingTableCell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    private TableColumn<Orders, CheckBox> select;
    @FXML
    private TableColumn<Orders, String> bookPrice;
    @FXML
    private TableColumn<Orders, String> orderQuantity;
    @FXML
    private TableColumn<Orders, String> orderDate;
    @FXML
    private Text total;
    @FXML
    private Button exportButton;

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

        exportButton.setOnAction(e -> handleExport());

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
        // Add checkbox to each order
        orders.forEach(order -> order.setSelected(new CheckBox()));
        ObservableList<Orders> observableList = FXCollections.observableArrayList(orders);

        bookName.setCellFactory(WrappingTableCell::new);

        // Configure the select column to show checkboxes
        select.setCellValueFactory(new PropertyValueFactory<>("selected"));

        bookName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookName()));
        bookPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f",
                        cellData.getValue().getPrice())));
        orderQuantity.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
        orderDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderDate()));

        tableView.setItems(observableList);
    }

    private void handleExport() {
        List<Orders> selectedOrders = tableView.getItems().stream()
                .filter(order -> order.getSelected().isSelected())
                .collect(Collectors.toList());

        if (selectedOrders.isEmpty()) {
            // Show an alert that no orders are selected
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Orders CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            exportToCSV(selectedOrders, file);
        }
    }

    private void exportToCSV(List<Orders> orders, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            // Write CSV header
            writer.write("Book Name,Price,Quantity,Order Date\n");

            // Write order data
            for (Orders order : orders) {
                writer.write(String.format("\"%s\",%f,%d,\"%s\"\n",
                        order.getBookName(),
                        order.getPrice(),
                        order.getQuantity(),
                        order.getOrderDate()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert
        }
    }


}
