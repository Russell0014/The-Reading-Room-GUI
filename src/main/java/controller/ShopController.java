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
import javafx.stage.Stage;
import model.Book;
import model.Model;

public class ShopController {
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
    private MenuItem logout;
    @FXML
    private TableView<Book> tableView; // Specify the type
    @FXML
    private TableColumn<Book, String> bookName; // Specify the type
    @FXML
    private TableColumn<Book, String> bookPrice;

    public ShopController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    // Add your code to complete the functionality of the program
    @FXML
    public void initialize() {
        loadBooks();
        home.setOnAction(e -> NavigationHandler.handleHomeAction(stage, model));
        shop.setOnAction(e -> NavigationHandler.handleShopAction(stage, model));
        logout.setOnAction(e -> NavigationHandler.handleLoginAction(stage, model));

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Home");
        stage.show();
    }

    private void loadBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList(model.getBookShop().getBooks());

        // Set up the TableView columns
        bookName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        bookPrice.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));

        // Set the items in the TableView
        tableView.setItems(books);
    }


}
