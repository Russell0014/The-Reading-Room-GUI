package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import utils.WrappingTableCell;

import java.util.HashMap;
import java.util.Map;

public class ShopController {
    private Model model;
    private Stage stage;
    private Stage parentStage;
    private Map<Book, TextField> quantityFields = new HashMap<>();


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
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> bookName;
    @FXML
    private TableColumn<Book, String> bookPrice;
    @FXML
    private TableColumn<Book, TextField> quantityColumn;
    @FXML
    private TableColumn<Book, Button> addToCartColumn;

    public ShopController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {
        loadBooks();
        home.setOnAction(e -> NavigationHandler.handleHomeAction(stage, model));
        shop.setOnAction(e -> NavigationHandler.handleShopAction(stage, model));
        cart.setOnAction(e -> NavigationHandler.handleCartAction(stage, model));
        orders.setOnAction(e -> NavigationHandler.handleOrdersAction(stage, model));
        logout.setOnAction(e -> NavigationHandler.handleLoginAction(stage, model));
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Shop");
        stage.show();
    }

    private void loadBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList(model.getBookShop().getBooks());

        // Set up the TableView columns
        bookName.setCellFactory(WrappingTableCell::new);
        bookName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        bookPrice.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getPrice())));

        // Configure the quantity column with text field cells
        quantityColumn.setCellFactory(col -> new TableCell<Book, TextField>() {
            private final TextField textField = new TextField("1");

            {
                // Configure the TextField
                textField.setPrefWidth(80);
                textField.setStyle("-fx-alignment: CENTER;");

                // Add input validation for numbers only
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    // Allow empty field while typing
                    if (newValue.isEmpty()) {
                        return;
                    }

                    // If not empty, ensure it's a valid number
                    if (!newValue.matches("\\d+")) {
                        textField.setText(oldValue);
                    }
                });

                // When focus is lost, ensure there's at least a 1
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {  // Focus lost
                        if (textField.getText().isEmpty()) {
                            textField.setText("1");
                        } else if (Integer.parseInt(textField.getText()) < 1) {
                            textField.setText("1");

                        }
                    }
                });
            }

            @Override
            protected void updateItem(TextField item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                    quantityFields.put(book, textField);
                    setGraphic(textField);
                }
            }
        });

        // Configure the Add to Cart button column
        addToCartColumn.setCellFactory(col -> new TableCell<Book, Button>() {
            private final Button addButton = new Button("Add to Cart");

            {
                addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                addButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    TextField quantityField = quantityFields.get(book);

                    int quantity = Integer.parseInt(quantityField.getText());

                    // cart logic:
                    model.addBookToCart(book.getTitle(), quantity, book.getPrice());

                    // Show confirmation alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText(quantity + "x " + book.getTitle() + " added to cart!");
                    alert.showAndWait();
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                }
            }
        });

        // Set the items in the TableView
        tableView.setItems(books);
    }
}