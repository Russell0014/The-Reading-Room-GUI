package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import java.util.HashMap;
import java.util.Map;

public class ShopController {
    private Model model;
    private Stage stage;
    private Stage parentStage;
    private Map<Book, TextField> quantityFields = new HashMap<>();

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

        // Configure the book name column with text wrapping
        bookName.setCellFactory(column -> {
            TableCell<Book, String> cell = new TableCell<Book, String>() {
                private Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        text.setWrappingWidth(column.getWidth() - 10); // 10 pixels padding
                        text.setTextAlignment(TextAlignment.LEFT);
                        setGraphic(text);
                    }
                }
            };

            // Update wrapping width when column is resized
            column.widthProperty().addListener((observable, oldValue, newValue) -> {
                Text text = (Text) cell.getGraphic();
                if (text != null) {
                    text.setWrappingWidth(newValue.doubleValue() - 10);
                }
            });

            return cell;
        });

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